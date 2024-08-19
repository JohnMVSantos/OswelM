/* Oswell backend Java Application.
 * 
 * Copyright (C) 2024 John Santos <johnmarivsantos@gmail.com>
 */

package project.oswel.server.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class AvatarController {

    @GetMapping("/index")
    public String getIndex(Model model){
        model.addAttribute("speakControl", 1);
        return "index";
    }

    
}
