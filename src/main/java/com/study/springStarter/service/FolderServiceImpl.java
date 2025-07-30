package com.study.springStarter.service;

import com.study.springStarter.dto.Folder;
import com.study.springStarter.mapper.FolderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {

    @Autowired
    FolderMapper mapper;

    @Override
    public int addFolder(Folder folder) throws Exception {
        return mapper.insertFolder(folder);
    }

    @Override
    public Folder findFolderById(int folderId) throws Exception {
        return mapper.selectFolderById(folderId);
    }

    @Override
    public List<Folder> findFoldersByUserId(int userId) throws Exception {
        return mapper.selectFoldersByUserId(userId);
    }

    @Override
    public int updateFolder(Folder folder) throws Exception {
        return mapper.updateFolder(folder);
    }

    @Override
    public int deleteFolder(int folderId) throws Exception {
        return mapper.deleteFolder(folderId);
    }
}
