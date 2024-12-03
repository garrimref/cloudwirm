package com.ref.cloudwirm.controller;

import com.ref.cloudwirm.domain.User;
import com.ref.cloudwirm.dto.S3ObjectMetaData;
import com.ref.cloudwirm.repos.UserRepository;
import com.ref.cloudwirm.service.SearchService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    private SearchService searchService;
    private UserRepository userRepository;
    public SearchController(SearchService searchService, UserRepository userRepository) {
        this.searchService = searchService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String search(@AuthenticationPrincipal UserDetails userDetails,
                         @RequestParam("query") String query,
                         Model model) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
        User user = userRepository.findByUsername(userDetails.getUsername());
        List<S3ObjectMetaData> results = searchService.search(user.getId(), query);

        model.addAttribute("searchResults", results);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("previousQuery", query);
        return "search";
    }
}
