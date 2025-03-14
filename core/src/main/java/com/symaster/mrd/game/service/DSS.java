package com.symaster.mrd.game.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.g2d.tansform.TransformInput;
import com.symaster.mrd.game.Groups;
import com.symaster.mrd.game.core.AiService;
import com.symaster.mrd.game.core.AliyunAiService;
import com.symaster.mrd.game.core.ai.AiMessage;
import com.symaster.mrd.game.core.ai.AiRequestType;
import com.symaster.mrd.game.core.ai.AiResponse;
import com.symaster.mrd.game.core.ai.SystemAiMessage;
import com.symaster.mrd.game.entity.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * 决策系统 (Decision Support System, DSS)
 * <p>小人的大脑</p>
 * <p>核心方法{@link DSS#logic(Creature, float)}</p>
 *
 * @author yinmiao
 * @since 2025/1/13
 */
public class DSS extends Node {

    private final List<Node> nodeList = new ArrayList<>();
    private AiService aiService;
    /**
     * 当前场景的数据库
     */
    private Database database;
    /**
     * 当前场景的游戏时间
     */
    private GameTime gameTime;

    @NotNull
    private static String getResultTemplate() {
        return "请选择你的行动，回复Json格式，下面是结构的说明：JSON结构如下所示： ```json { \"type\": \"动作类型\", // 动作类型可选：interaction（交互）、move_to（移动） \"dstId\": 目标ID, // 如果进行交互，则需要指定目标ID；否则可以省略 \"interaction_content\": { // 如果选择了交互，则需填写此部分 \"type\": \"说或做\", \"val\": \"具体内容\" }, \"move_vector\": { // 如果选择了移动，则需填写此部分 \"x\": Double, \"y\": Double } } ```";
    }

    @NotNull
    private static String getAiTips() {
        return "提示：如果你要和某个实体交互，你可直接交互而不需要移动过去后交互";
    }

    /**
     * 小人每帧会调用该方法
     *
     * @param nodes 调用的对象
     * @param delta 上一帧耗时
     */
    public void logic(Creature nodes, float delta) {
        // 当前DSS不可用
        if (dssNotAvailable()) {
            return;
        }

        // 当前角色正在执行动作
        if (action(nodes, delta)) {
            return;
        }

        AiResponse aiResponse = database.getAiResponse(nodes.getId());
        if (aiResponse != null) {
            if (aiResponse.getAiRequestType() == AiRequestType.NOT_STARTED ||
                    aiResponse.getAiRequestType() == AiRequestType.REQUESTING) {
                database.setNodeStatus(nodes.getId(), NodeStatusEnum.THINK);
                return;
            }
            if (aiResponse.getAiRequestType() == AiRequestType.FINISH) {
                // AI生成完成
                String aiResult = aiResponse.getContent().toString();

                if (aiResult.startsWith("```json")) {
                    aiResult = aiResult.substring(7, aiResult.length() - 3);
                }

                try {
                    DSSResult dssResult = JSONObject.parseObject(aiResult, DSSResult.class);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


                return;
            }

            database.setAiResponse(nodes.getId(), null);
            return;
        }

        AiResponse aiResponse1 = new AiResponse();
        aiResponse1.setContent(new StringBuilder());
        aiResponse1.setReasoningContent(new StringBuilder());
        aiService.stream(aiResponse1, getParam(nodes), "qwen-max");
        database.setAiResponse(nodes.getId(), aiResponse1);
        database.setNodeStatus(nodes.getId(), NodeStatusEnum.THINK);
    }

    private List<AiMessage> getParam(Creature nodes) {
        ArrayList<AiMessage> rtn = new ArrayList<>();

        String s1 = "你是%s族，你叫%s，性别%s，你当前位置{x:%.2f, y:%.2f}";

        String formatS1 = String.format(s1, nodes.getRace().getName(), nodes.getName(), nodes.getGender().getName(),
                                        nodes.getPositionX(), nodes.getPositionY());

        JSONArray objects = getNodeJson(nodes);

        String s2 = "你的视线范围内";
        if (objects.isEmpty()) {
            s2 = s2 + "什么都没有";
        } else {
            s2 = s2 + "有" + objects.toJSONString();
        }

        rtn.add(new SystemAiMessage(formatS1 + "\n" + s2 + "\n" + getResultTemplate() + "\n" + getAiTips()));


        return rtn;
    }

    @NotNull
    private JSONArray getNodeJson(Creature nodes) {
        JSONArray objects = new JSONArray();
        for (Block activeBlock : getScene().getActiveBlocks()) {
            getScene().findNodesByBlock(activeBlock, nodeList, true);
        }

        for (Node node : nodeList) {
            if (node.getId() == nodes.getId()) {
                continue;
            }

            if (node instanceof Creature) {
                Creature creature = (Creature) node;

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", creature.getId());
                jsonObject.put("name", creature.getName());
                jsonObject.put("position",
                               String.format("{x:%.2f,y%.2f}", creature.getPositionX(), creature.getPositionY()));
                NodeStatusEnum nodeStatus = database.getNodeStatus(creature.getId());
                if (nodeStatus != null) {
                    jsonObject.put("status", "对方正在" + nodeStatus.getDesc());
                }
                objects.add(jsonObject);
            }
        }
        return objects;
    }

    /**
     * 指定角色是否正在执行动作
     *
     * @return true: 当前角色执行动作；false: 当前角色没有执行动作
     */
    private boolean action(Creature nodes, float delta) {
        Queue<NodeActionData> nodeActionData = database.getNodeActionData(nodes.getId());
        NodeActionData peek = nodeActionData.peek();

        if (peek == null) {
            return false;
        }

        // 等待
        if (peek.getNodeActionEnum() == NodeActionEnum.WAIT) {
            if (peek.getWaitSecond() == null) {
                nodeActionData.poll();
                return false;
            }

            if (gameTime.getTime() >= peek.getWaitSecond()) {
                nodeActionData.poll();
            }

            return true;
        } else

            // 移动
            if (peek.getNodeActionEnum() == NodeActionEnum.MOVE) {
                if (peek.getMoveToX() == null || peek.getMoveToY() == null) {
                    nodeActionData.poll();
                    return false;
                }

                List<TransformInput> node = nodes.getNode(TransformInput.class);
                if (node.isEmpty()) {
                    return true;
                }

                node.get(0)
                    .getVector2()
                    .set(peek.getMoveToX() - nodes.getPositionX(), peek.getMoveToY() - nodes.getPositionY());
                return true;
            }


        return false;
    }

    /**
     * DSS是否不可用，
     *
     * @return 是否不可用
     */
    public boolean dssNotAvailable() {
        return getScene() == null || gameTime == null || database == null || aiService == null;
    }

    /**
     * 当前节点被添加进场景事件
     *
     * @param scene 场景
     */
    @Override
    public void onScene(Scene scene) {
        super.onScene(scene);

        Set<Node> byGroup = getScene().getByGroup(Groups.DATABASE);
        if (byGroup == null) {
            getScene().add(new Database(), Groups.DATABASE);
        }

        database = (Database) getScene().getByGroup(Groups.DATABASE).iterator().next();

        Set<Node> byGroup1 = scene.getByGroup(Groups.TIMER);
        if (byGroup1 != null) {
            gameTime = (GameTime) byGroup1.iterator().next();
        }
    }

    /**
     * 当前场景被移除场景事件
     *
     * @param scene 场景
     */
    @Override
    public void extScene(Scene scene) {
        super.extScene(scene);
        this.database = null;
        this.gameTime = null;
    }

    @Override
    public void dispose() {
        super.dispose();

        if (this.aiService != null) {
            try {
                this.aiService.close();
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public void create() {
        super.create();
        this.aiService = new AliyunAiService();
    }

}
