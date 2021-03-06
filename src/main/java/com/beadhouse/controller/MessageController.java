package com.beadhouse.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.beadhouse.in.*;
import com.beadhouse.utils.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.beadhouse.out.BasicData;
import com.beadhouse.service.SendMessageService;
import com.beadhouse.service.impl.ElderServiceImpl;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class MessageController {

    @Value("${google.prifix}")
    private String prifix;

    @Autowired
    SendMessageService sendMessageService;
    @Resource
    private FFMpegMusicUtil ffMpegMusicUtil;

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);


    /**
     * 发送消息
     *
     * @param request
     * @return
     */
    @RequestMapping("sendMessage")
    @ResponseBody
    public BasicData sendMessage(SendMessageParam param, HttpServletRequest request) {
        if (param.getToken() == null) param = new Gson().fromJson(request.getParameter("json"), SendMessageParam.class);
        MultipartFile file = UploadUtil.getFile(request);
        File scratchFile = null;
        if (file != null) {
            try {
                String fileName = file.getOriginalFilename();
                String suffic = fileName.substring(fileName.lastIndexOf("."));
                scratchFile = File.createTempFile("scratchFile", suffic, new File(prifix));
                InputStream inputStream = file.getInputStream();
                FileUtils.copyInputStreamToFile(inputStream, scratchFile);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return sendMessageService.sendMessage(param, scratchFile);
    }

    /**
     * 获取聊天记录
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("getChatHistory")
    @ResponseBody
    public BasicData getChatHistory(@Valid @RequestBody GetMessageParam param, HttpServletRequest request) {
        return sendMessageService.getChatHistory(param);
    }

    /**
     * 获取老人端聊天记录
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("elder/getChatHistory")
    @ResponseBody
    public BasicData getElderChatHistory(@Valid @RequestBody GetMessageParam param, HttpServletRequest request) {
        return sendMessageService.getElderChatHistory(param);
    }


    /**
     * 修改聊天记录
     */
    @RequestMapping("updateChatHistory")
    @ResponseBody
    public BasicData updateChatHistory(@Valid @RequestBody UpdateChatParam param, HttpServletRequest request) {

        return sendMessageService.updateChatHistory(param);
    }

    /**
     * 老人修改聊天记录
     */
    @RequestMapping("elder/updateChatHistory")
    @ResponseBody
    public BasicData updateChatHistoryByElder(@Valid @RequestBody UpdateChatParam param, HttpServletRequest request) {

        return sendMessageService.updateChatHistoryByElder(param);
    }

    /**
     * 再问一次
     */
    @RequestMapping("askAgain")
    @ResponseBody
    public BasicData askAgain(@Valid @RequestBody AskAgainParam param, HttpServletRequest request) {

        return sendMessageService.askAgain(param);
    }

    /**
     * 回答问题
     *
     * @param request
     * @return
     */
    @RequestMapping("answerQuestion")
    @ResponseBody
    public BasicData answerQuestion(AnswerQuestParam param, HttpServletRequest request) {
        if (param.getToken() == null) param = new Gson().fromJson(request.getParameter("json"), AnswerQuestParam.class);
        System.out.println("token = " + param.getToken());
        //上传文件至后台
        MultipartFile file = UploadUtil.getFile(request);
        File scratchFile = null;
        if (file != null) {
            try {
                String fileName = file.getOriginalFilename();
                String suffic = fileName.substring(fileName.lastIndexOf("."));
                scratchFile = File.createTempFile("scratchFile", suffic, new File(prifix));
                InputStream inputStream = file.getInputStream();
                FileUtils.copyInputStreamToFile(inputStream, scratchFile);
                System.out.println("------" + scratchFile);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return sendMessageService.answerQuestion(param, scratchFile);
    }


}
