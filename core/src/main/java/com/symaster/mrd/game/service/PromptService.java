package com.symaster.mrd.game.service;

import com.badlogic.gdx.Gdx;
import com.symaster.mrd.util.ModifyOptionsEnum;

/**
 * @author yinmiao
 * @since 2025/5/1
 */
public class PromptService {

    private final String mainPrompt;
    private final String chatInfo;
    private final String statusInfo;

    public PromptService() {
        this.mainPrompt = Gdx.files.internal("prompt/main").readString();
        this.chatInfo = Gdx.files.internal("prompt/chat_info").readString();
        this.statusInfo = Gdx.files.internal("prompt/status_info").readString();
    }

    public String buildPrompt(String name,
                              String gender,
                              String age,
                              String hp,
                              String tp,
                              String atk,
                              String background,
                              String dialogue,
                              String user) {
        String rtn = mainPrompt;

        rtn = rtn.replace("{{chat}}", name);
        rtn = rtn.replace("{{InstructionDesc}}", ModifyOptionsEnum.disc());
        rtn = rtn.replace("{{ChatInfo}}", buildChatInfo(name, gender, age, hp, tp, atk, background));
        rtn = rtn.replace("{{StatusInfo}}", buildStatusInfo(dialogue, user));

        return rtn;
    }

    private String buildStatusInfo(String dialogue, String user) {
        String info = statusInfo;

        info = info.replace("{{dialogue}}", dialogue);
        info = info.replace("{{user}}", user);

        return info;
    }

    private String buildChatInfo(String name,
                                 String gender,
                                 String age,
                                 String hp,
                                 String tp,
                                 String atk,
                                 String background) {
        String info = chatInfo;

        info = info.replace("{{chat}}", name);
        info = info.replace("{{gender}}", gender);
        info = info.replace("{{age}}", age);
        info = info.replace("{{hp}}", hp);
        info = info.replace("{{tp}}", tp);
        info = info.replace("{{atk}}", atk);
        info = info.replace("{{background}}", background);

        return info;
    }

}
