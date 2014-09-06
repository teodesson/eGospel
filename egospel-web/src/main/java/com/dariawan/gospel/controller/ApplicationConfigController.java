/**
 * eGospel Project
 * Copyright (C) 2014 Desson Ariawan <teodesson@gmail.com>
 *
 * PLEASE NOTE: Your use of this software is subject to the terms and conditions of the license agreement by which you acquired this software.
 * You may not use this software if you have not validly acquired a license for the software from Desson Ariawan or its licensed distributors.
 */
package com.dariawan.gospel.controller;

import java.io.File;
import java.net.URI;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriTemplate;

import com.dariawan.gospel.domain.ApplicationConfig;
import com.dariawan.gospel.service.GospelService;
import com.google.common.io.Files;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;

@Controller
public class ApplicationConfigController {

    private static final String ESC_CHAR_DOT = "\\.";
    private static final long FILE_SIZE_RESUME = 46166;
    private static final long FILE_SIZE_PHOTO = 30886;
    @Autowired
    private GospelService gospelService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/config/{id}/files")
    @ResponseBody
    public Map<String, String> uploadFiles(
            @PathVariable String id,
            @RequestParam String description,
            @RequestParam MultipartFile resume,
            @RequestParam MultipartFile photo) throws Exception {
        ApplicationConfig config = gospelService.findApplicationConfigById(id);
        if (config == null) {
            throw new IllegalStateException();
        }

        Map<String, String> result = new HashMap<>();

        logger.info("Resume => Content-Type : {}, Filename : {}, Size : {}", new Object[]{
                    resume.getContentType(), resume.getOriginalFilename(), resume.getSize()});

        logger.info("Photo => Content-Type : {}, Filename : {}, Size : {}", new Object[]{
                    photo.getContentType(), photo.getOriginalFilename(), photo.getSize()});

        File resumeTarget = File.createTempFile(resume.getOriginalFilename().split(ESC_CHAR_DOT)[0], "." + resume.getOriginalFilename().split(ESC_CHAR_DOT)[1]);
        File photoTarget = File.createTempFile(photo.getOriginalFilename().split(ESC_CHAR_DOT)[0], "." + photo.getOriginalFilename().split(ESC_CHAR_DOT)[1]);
        resume.transferTo(resumeTarget);
        photo.transferTo(photoTarget);

        logger.info("Resume saved to {}", resumeTarget.getAbsolutePath());
        logger.info("Photo saved to {}", photoTarget.getAbsolutePath());

        //System.out.println(resume.getSize());
        if (resume.getSize() == FILE_SIZE_RESUME) {
            result.put("resume", "success");
        } else {
            result.put("resume", "error in size");
        }

        //System.out.println(photo.getSize());
        if (photo.getSize() == FILE_SIZE_PHOTO) {
            result.put("photo", "success");
        } else {
            result.put("photo", "error in size");
        }

        //System.out.println(description);
        if ("John's files".equals(description)) {
            result.put("description", "success");
        } else {
            result.put("description", "wrong content");
        }

        return result;
    }

    @RequestMapping("/config/{id}")
    @ResponseBody
    public ApplicationConfig findApplicationConfigById(@PathVariable String id) {
        ApplicationConfig config = gospelService.findApplicationConfigById(id);
        if (config == null) {
            throw new IllegalStateException();
        }
        return config;
    }

    @RequestMapping(value = "/config", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid ApplicationConfig config, HttpServletRequest request, HttpServletResponse response) {
        gospelService.save(config);
        String requestUrl = request.getRequestURL().toString();
        URI uri = new UriTemplate("{requestUrl}/{id}").expand(requestUrl, config.getId());
        response.setHeader("Location", uri.toASCIIString());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/config/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable String id, @RequestBody @Valid ApplicationConfig config) {
        ApplicationConfig a = gospelService.findApplicationConfigById(id);
        if (a == null) {
            logger.warn("Config with id [{}] not found", id);
            throw new IllegalStateException();
        }
        config.setId(a.getId());
        gospelService.save(config);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/config/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id) {
        ApplicationConfig a = gospelService.findApplicationConfigById(id);
        if (a == null) {
            logger.warn("Config with id [{}] not found", id);
            throw new IllegalStateException();
        }
        gospelService.delete(a);
    }

    @RequestMapping(value = "/config", method = RequestMethod.GET)
    @ResponseBody
    public List<ApplicationConfig> findAll(
            @RequestParam(required = false) String search,
            Pageable pageable,
            HttpServletResponse response) {
        
        if (StringUtils.hasText(search)) {
            return gospelService.findApplicationConfigs(search, pageable).getContent();
        } else {
            return gospelService.findAllApplicationConfigs(pageable).getContent();
        }

    }

    @RequestMapping(value = "/config/upload", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> testUpload(@RequestParam(value = "uploadedfiles[]") List<MultipartFile> listPhoto) throws Exception {

        logger.debug("Number file(s) uploaded {}", listPhoto.size());

        List<Map<String, String>> result = new ArrayList<>();

        for (MultipartFile multipartFile : listPhoto) {
            logger.debug("Filename : {}", multipartFile.getName());
            logger.debug("Original Filename : {}", multipartFile.getOriginalFilename());
            logger.debug("File size : {}", multipartFile.getSize());

            Map<String, String> description = new HashMap<>();
            description.put("File Name", multipartFile.getOriginalFilename());
            description.put("File Size", Long.valueOf(multipartFile.getSize()).toString());
            description.put("Content Type", multipartFile.getContentType());
            description.put("UUID", UUID.randomUUID().toString());
            File temp = File.createTempFile("xxx", "xxx");
            multipartFile.transferTo(temp);
            description.put("MD5", createMD5Sum(temp));
            result.add(description);
        }

        return result;
    }

    private String createMD5Sum(File file) throws Exception {
        byte[] checksum = Files.getDigest(file, MessageDigest.getInstance("MD5"));
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < checksum.length; i++) {
            result.append(Integer.toString((checksum[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({IllegalStateException.class})
    public void handle() {
        logger.debug("Config with specified name not found");
    }
}
