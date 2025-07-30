package com.study.springStarter.service;

import com.study.springStarter.dto.Folder;

import java.util.List;

public interface FolderService {

    int addFolder(Folder folder) throws Exception;

    Folder findFolderById(int folderId) throws Exception;

    List<Folder> findFoldersByUserId(int userId) throws Exception;

    int updateFolder(Folder folder) throws Exception;

    int deleteFolder(int folderId) throws Exception;

}
