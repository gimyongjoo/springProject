package com.study.springStarter.mapper;

import com.study.springStarter.dto.Folder;

import java.util.List;

public interface FolderMapper {

    int insertFolder(Folder folder) throws Exception;

    Folder selectFolderById(int folderId) throws Exception;

    List<Folder> selectFoldersByUserId(int userId) throws Exception;

    int updateFolder(Folder folder) throws Exception;

    int deleteFolder(int folderId) throws Exception;

}
