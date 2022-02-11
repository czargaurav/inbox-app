package io.czargaurav.inbox.controllers;

import io.czargaurav.inbox.email.Email;
import io.czargaurav.inbox.email.EmailRepository;
import io.czargaurav.inbox.folders.Folder;
import io.czargaurav.inbox.folders.FolderRepository;
import io.czargaurav.inbox.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class EmailViewController {

    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private FolderService folderService;

    @GetMapping("/emails/{id}")
    public String emailView(@PathVariable UUID id, @AuthenticationPrincipal OAuth2User principal, Model model) {
        if( principal == null || !StringUtils.hasText(principal.getAttribute("login")) ) {
            return "index";
        }

//        Fetch Folders
        String userId = principal.getAttribute("login");
        List<Folder> userFolders = folderRepository.findAllById(userId);
        model.addAttribute("userFolders", userFolders);
        List<Folder> defaultFolders = folderService.fetchAllFolders(userId);
        model.addAttribute("defaultFolders", defaultFolders);

        Optional<Email> optionalEmail = emailRepository.findById(id);
        if(!optionalEmail.isPresent()) {
            return "index";
        }
        Email email = optionalEmail.get();
        String toIds = String.join(", ", email.getTo());
        model.addAttribute("email", email);
        model.addAttribute("toIds", toIds);

        return "email-page";

    }
}
