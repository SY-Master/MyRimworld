package com.symaster.mrd.game.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.symaster.ai.AiRequestType;
import com.symaster.ai.AiResponse;
import com.symaster.ai.AiService;
import com.symaster.ai.OpenaiAiService;
import com.symaster.ai.message.AiMessage;
import com.symaster.ai.message.SystemAiMessage;
import com.symaster.mrd.g2d.Block;
import com.symaster.mrd.g2d.Node;
import com.symaster.mrd.g2d.scene.Scene;
import com.symaster.mrd.g2d.tansform.TransformInput;
import com.symaster.mrd.game.GameSingleData;
import com.symaster.mrd.game.Groups;
import com.symaster.mrd.game.entity.*;
import com.symaster.mrd.util.GeomUtil;
import com.symaster.mrd.util.SceneUtil;
import com.symaster.mrd.util.StringUtil;
import com.symaster.mrd.util.UnitUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static com.symaster.mrd.game.entity.NodeActionEnum.THINK;
import static com.symaster.mrd.util.UnitUtil.ofM;

/**
 * 决策系统 (Decision Support System, DSS)
 * <p>小人的大脑</p>
 * <p>核心方法{@link DSS#logic(Creature, float)}</p>
 *
 * @author yinmiao
 * @since 2025/1/13
 */
public class DSS extends Node {

    private static final Logger logger = LoggerFactory.getLogger(DSS.class);
    private final List<Node> nodeList = new ArrayList<>();
    /**
     * 选择大模型
     */
    public String aiModel = "qwen-max";
    /**
     * 是否启用AI
     */
    public boolean enableAi = false;
    private AiService aiService;
    /**
     * 当前场景的数据库
     */
    private Database database;
    /**
     * 当前场景的游戏时间
     */
    private GameTime gameTime;
    /**
     * 主角
     */
    private Node player;

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

        // 指令处理
        if (action(nodes, delta)) {
            return;
        }

        // 思考是否正在冷却
        if (thinkCooling()) {
            return;
        }

        logger.info("{} 开始主动思考", nodes.getName());
        aiThink(nodes, getParam(nodes));
    }

    private boolean thinkCooling() {
        if (!enableAi) {
            // 如果禁用了AI
            return true;
        }

        if (database.getThinkCooling() == null) {
            return false;
        }

        return database.getThinkCooling() >= gameTime.getTime();
    }

    /**
     * 消息处理
     *
     * @param nodes 需要接收消息的节点
     * @return true：处理了消息或还有消息要处理，false：没有消息需要处理
     */
    private boolean messageHandler(Creature nodes) {
        // 是否有消息
        if (database.getMessageQueue().peek(nodes.getGlobalId()) == null) {
            return false;
        }

        // 接收到消息
        MessageResult msg = database.getMessageQueue().poll(nodes.getGlobalId());

        // DSS消息内容
        DSSInteractionContent dssInteractionContent = msg.getDssInteractionContent();

        Gender srcGender;
        String srvName;

        Node srcNode = getScene().getNodeById(msg.getSrcId());
        if (srcNode instanceof Creature) {
            Creature node = (Creature) srcNode;
            srcGender = node.getGender();
            srvName = node.getName();
        } else {
            srcGender = null;
            srvName = null;
        }

        String memory = String.format("[%s年%s月%s日 %s时%s分] %s和你交互，%s%s：\"%s\"", gameTime.getYear(),
                                      gameTime.getMonth(), gameTime.getDay(), gameTime.getHour(), gameTime.getMinute(),
                                      srvName, srvName, dssInteractionContent.getType(),
                                      dssInteractionContent.getVal());
        database.addNodeActionData(nodes.getGlobalId(), new NodeActionData(NodeActionEnum.SAVE_MEMORY, memory));

        String memoryPrompt = getMemoryPrompt(nodes);

        String prompt = getMainPrompt(nodes) + "\n" + /*getResultTemplate() + "\n" + getAiTips() +*/ "\n" + memoryPrompt;

        List<AiMessage<?>> messages = Collections.singletonList(new SystemAiMessage<>(prompt));

        logger.info("{} 开始思考回复", nodes.getName());
        aiThink(nodes, messages);
        return true;
    }

    private void aiThink(Creature nodes, List<AiMessage<?>> messages) {
        AiResponse stream = aiService.stream(messages, "qwen-max");
        setThinkCooling();

        database.setAiResponse(nodes.getGlobalId(), stream);
        database.addNodeActionData(nodes.getGlobalId(), new NodeActionData(THINK, stream));
        // database.setNodeStatus(nodes.getId(), NodeStatusEnum.THINK);
    }

    private void setThinkCooling() {
        database.setThinkCooling(gameTime.getTimeByHour(1));
    }

    private String getMemoryPrompt(Creature nodes) {
        // 获取该节点的所有记忆
        String nodeMemory = String.join("\n", database.getNodeMemory().getAll(nodes.getGlobalId()));

        // 记忆信息
        String memoryPrompt;
        if (nodeMemory.isEmpty()) {
            memoryPrompt = "";
        } else {
            memoryPrompt =
                    "# Memory\n在回答用户问题时，请尽量忘记大部分不相关的信息。只有当用户提供的信息与当前问题或对话内容非常相关时，才记住这些信息并加以使用。请确保你的回答简洁、准确，并聚焦于用户当前的问题或对话主题。信息：\n" +
                            nodeMemory;
        }
        return memoryPrompt;
    }

    /**
     * 发去移动指令
     *
     * @param nodes   移动目标节点
     * @param vectorX 移动向量
     * @param vectorY 移动向量
     */
    public void moveTo(Creature nodes, float vectorX, float vectorY) {

        Node topParent = SceneUtil.getTopParent(nodes);

        // 发起移动指令
        database.addNodeActionData(nodes.getGlobalId(),
                                   new NodeActionData(NodeActionEnum.MOVE, topParent.getPositionX() + vectorX,
                                                      topParent.getPositionY() + vectorY));

        logger.info("发布移动指令 {}, vector(x:{}, y:{})", nodes.getName(), vectorX, vectorY);
    }

    public void interaction(Creature nodes, DSSResult dssResult) {
        DSSInteractionContent interactionContent = dssResult.getInteractionContent();
        if (interactionContent == null) {
            return;
        }

        String dstId = interactionContent.getDstId();

        if (dstId == null) {
            return;
        }

        String dstName;
        Node nodeById = getScene().getNodeById(dstId);
        if (nodeById instanceof Creature) {
            dstName = ((Creature) nodeById).getName();
        } else {
            dstName = "不可描述的存在";
        }

        Node topParent = SceneUtil.getTopParent(nodeById);

        float positionX = topParent.getPositionX();
        float positionY = topParent.getPositionY();

        // 距离大于两米，需要先发起移动指令
        if (GeomUtil.distance(nodes.getPositionX(), nodes.getPositionY(), positionX, positionY) > ofM(2)) {
            database.addNodeActionData(nodes.getGlobalId(),
                                       new NodeActionData(NodeActionEnum.MOVE, positionX + ofM(0.5f), positionY));
        }

        StartSessionObj startSessionObj = new StartSessionObj();
        startSessionObj.setDstIds(Collections.singletonList(dstId));
        startSessionObj.setSessionId(UUID.randomUUID().toString());

        // 发起会话指令
        database.addNodeActionData(nodes.getGlobalId(),
                                   new NodeActionData(NodeActionEnum.START_SESSION, startSessionObj));

        // 发起交互指令
        database.addNodeActionData(nodes.getGlobalId(), new NodeActionData(NodeActionEnum.INTERACTION, dssResult));

        String memory = String.format("[%s年%s月%s日 %s时%s分] 你和%s交互，你%s\"%s\"", gameTime.getYear(),
                                      gameTime.getMonth(), gameTime.getDay(), gameTime.getHour(), gameTime.getMinute(),
                                      dstName, dssResult.getInteractionContent().getType(),
                                      dssResult.getInteractionContent().getVal());
        database.addNodeActionData(nodes.getGlobalId(), new NodeActionData(NodeActionEnum.SAVE_MEMORY, memory));
    }

    /**
     * 添加等待指令
     *
     * @param nodes 指定节点
     */
    private void nodeWaitAction(Creature nodes) {
        // 等待15分钟（游戏时间）
        database.addNodeActionData(nodes.getGlobalId(),
                                   new NodeActionData(NodeActionEnum.WAIT, gameTime.getTimeByMinute(15)));
    }

    private List<AiMessage<?>> getParam(Creature nodes) {
        ArrayList<AiMessage<?>> rtn = new ArrayList<>();

        String mainPrompt = GameSingleData.promptService.buildPrompt(nodes.getName(), nodes.getGender().getName(), "20",
                                                                     nodes.getHp().desc(), nodes.getEnergy().desc(),
                                                                     "23", nodes.getRoleDesc(), "false", "无");

        rtn.add(new SystemAiMessage<>(getMainPrompt(nodes)));
        return rtn;
    }

    private String getMainPrompt(Creature nodes) {
        String s1 = "你是%s族，你叫%s，性别%s，你当前位置{x:%.2f, y:%.2f}";

        String formatS1 = String.format(s1, nodes.getRace().getName(), nodes.getName(), nodes.getGender().getName(),
                                        UnitUtil.ofScene(nodes.getPositionX()), UnitUtil.ofScene(nodes.getPositionY()));

        String s2 = "你的视线范围内";

        JSONArray objects = getNodeJson(nodes);

        String s3;
        if (objects.isEmpty()) {
            s3 = "什么都没有";
        } else {
            s3 = "有" + objects.toJSONString();
        }

        return formatS1 + "\n" + s2 + s3;
    }

    private JSONArray getNodeJson(Creature nodes) {
        nodeList.clear();

        JSONArray objects = new JSONArray();
        for (Block activeBlock : getScene().getActiveBlocks()) {
            getScene().findNodesByBlock(activeBlock, nodeList, true);
        }

        for (Node node : nodeList) {
            if (node.getGlobalId().equals(nodes.getGlobalId())) {
                continue;
            }

            if (node instanceof Creature) {
                Creature creature = (Creature) node;

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", creature.getGlobalId());
                jsonObject.put("name", creature.getName());
                jsonObject.put("position", String.format("{x:%.2f,y%.2f}", UnitUtil.ofScene(creature.getPositionX()),
                                                         UnitUtil.ofScene(creature.getPositionY())));
                jsonObject.put("性别", creature.getGender().getName());
                objects.add(jsonObject);
            }
        }
        return objects;
    }

    /**
     * 指令处理
     *
     * @return 返回是否还有指令未处理
     */
    private boolean action(Creature nodes, float delta) {
        Queue<NodeActionData> nodeActionData = database.getNodeActionData(nodes.getGlobalId());

        // 如果没有指令，则返回false
        NodeActionData peek = nodeActionData.peek();
        if (peek == null) {
            // 查看是否收到消息
            return messageHandler(nodes);
        }

        // 没有在思考，并且有收到消息
        if (peek.getNodeActionEnum() != THINK && messageHandler(nodes)) {
            return true;
        }

        // 指令是否执行完成，如果执行完成，则从队列中移除
        boolean over = false;

        switch (peek.getNodeActionEnum()) {
            case MOVE: { // 移动指令
                over = actionMove(peek, nodes, delta);
                break;
            }
            case WAIT: { // 等待指令
                over = actionWait(peek, nodes, delta);
                break;
            }
            case INTERACTION: { // 交互指令
                over = actionInteraction(peek, nodes, delta);
                break;
            }
            case THINK: { // 思考指令（正在调用AI）
                over = actionThink(peek, nodes, delta);
                break;
            }
            case SAVE_MEMORY: { // 保存记忆指令
                over = actionSaveMemory(peek, nodes, delta);
            }
            case START_SESSION: {
                over = actionStartSession(peek, nodes, delta);
            }
        }

        if (over) {
            nodeActionData.poll();
        }

        return !nodeActionData.isEmpty();
    }

    private boolean actionStartSession(NodeActionData peek, Creature nodes, float delta) {
        return false;
    }

    private boolean actionSaveMemory(NodeActionData peek, Creature nodes, float delta) {
        database.getNodeMemory().add(nodes.getGlobalId(), peek.getNodeMemory());
        logger.info("{} 增加记忆 {} ", nodes.getName(), peek.getNodeMemory());
        return true;
    }

    private boolean actionThink(NodeActionData peek, Creature nodes, float delta) {
        AiResponse aiResponse = peek.getAiResponse();
        if (aiResponse == null) {
            return true;
        }

        if (aiResponse.getAiRequestType() != AiRequestType.END) {
            // 未思考结束
            return false;
        }

        // 思考结束（AI生成完成）设置思考冷却
        setThinkCooling();

        try {
            String aiResult = StringUtil.repairJsonStr(aiResponse.getContent().toString());

            logger.info("{} 思考完成 {}", nodes.getName(), aiResult);

            aiResponseStrHandler(nodes, aiResult);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return true;
    }

    private void aiResponseStrHandler(Creature nodes, String aiResult) {
        DSSResult dssResult = JSONObject.parseObject(aiResult, DSSResult.class);
        if (dssResult.getType().equalsIgnoreCase("interaction")) {
            // 和指定目标交互
            interaction(nodes, dssResult);
        } else if (dssResult.getType().equalsIgnoreCase("move_to")) {
            // 移动到指定位置
            DSSVector moveVector = dssResult.getMoveVector();
            if (moveVector != null && moveVector.getX() != null && moveVector.getY() != null) {
                moveTo(nodes, UnitUtil.ofM(moveVector.getX()), UnitUtil.ofM(moveVector.getY()));
            }
        }
    }

    private boolean actionInteraction(NodeActionData peek, Creature nodes, float delta) {
        if (peek.getDssResult() != null && peek.getDssResult().getInteractionContent() != null &&
                peek.getDssResult().getInteractionContent().getDstId() != null &&
                peek.getDssResult().getInteractionContent().getType() != null &&
                !peek.getDssResult().getInteractionContent().getType().isEmpty()) {

            MessageQueue messageQueue = database.getMessageQueue();
            messageQueue.send(nodes.getGlobalId(), peek.getDssResult().getInteractionContent().getDstId(),
                              peek.getDssResult().getInteractionContent());
        }

        return true;
    }

    private boolean actionWait(NodeActionData peek, Creature nodes, float delta) {
        if (peek.getWaitSecond() == null) {
            // 未设置等待时间，指令执行完成
            return true;
        }

        return gameTime.getTime() >= peek.getWaitSecond();
    }

    private boolean actionMove(NodeActionData peek, Creature nodes, float delta) {
        if (peek.getMoveToX() == null || peek.getMoveToY() == null) {
            // 未给出明确坐标，指令执行完成
            logger.info("移动指令错误结束 {}", nodes.getName());
            return true;
        }

        List<TransformInput> node = nodes.getNode(TransformInput.class);
        if (node.isEmpty()) {
            // 指定节点不存在，指令无法完成
            logger.info("移动指令错误结束，不存在移动输入组件 {}", nodes.getName());
            return true;
        }

        TransformInput transformInput = node.get(0);

        if (GeomUtil.distance(nodes.getPositionX(), nodes.getPositionY(), peek.getMoveToX(), peek.getMoveToY()) <
                ofM(0.1f)) {
            // 如果与目标坐标小于0.1米，则认为已经到达目标位置，指令执行完成

            transformInput.getVector2().set(0, 0);

            logger.info("移动指令完成 {}", nodes.getName());
            return true;
        }

        // 否则，设置移动向量
        transformInput.getVector2()
                      .set(peek.getMoveToX() - nodes.getPositionX(), peek.getMoveToY() - nodes.getPositionY());

        // 长度大于1才归一化，否则归一化角色移动会出现震荡
        if (transformInput.getVector2().len() > 1) {
            transformInput.getVector2().nor();
        }

        return false;
    }

    /**
     * DSS是否不可用，
     *
     * @return 是否不可用
     */
    public boolean dssNotAvailable() {
        if (getScene() == null) {
            return true;
        }

        if (gameTime == null) {
            return true;
        }

        if (database == null) {
            return true;
        }

        return enableAi && aiService == null;
    }

    /**
     * 当前节点被添加进场景事件
     *
     * @param scene 场景
     */
    @Override
    public void onScene(Scene scene) {
        super.onScene(scene);

        this.aiService = new OpenaiAiService("https://dashscope.aliyuncs.com/compatible-mode/v1",
                                             "sk-00020fc401b44687ac66d5a0ff933a2e");

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

        if (this.aiService != null) {
            try {
                this.aiService.close();
                this.aiService = null;
            } catch (IOException ignored) {
            }
        }
    }

    public void setPlayer(Node player) {
        this.player = player;
    }

}
