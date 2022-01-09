package io.czargaurav.inbox.service;

import io.czargaurav.inbox.folders.Folder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FolderService {

    public List<Folder> fetchAllFolders(String userId) {
        return Arrays.asList(
            new Folder(userId, "Inbox", "blue"),
            new Folder(userId, "Sent Items", "green"),
            new Folder(userId, "Important", "red")
        );
    }
}
