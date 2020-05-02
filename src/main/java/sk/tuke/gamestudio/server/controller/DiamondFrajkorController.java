package sk.tuke.gamestudio.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.context.annotation.Scope;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.diamond.frajkor.webui.Command;
import sk.tuke.gamestudio.game.diamond.frajkor.webui.WebUI;
import sk.tuke.gamestudio.service.score.ScoreService;

import java.util.List;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class DiamondFrajkorController {

    private final WebUI webUI;

    private final ScoreService scoreService;

    public DiamondFrajkorController(ScoreService scoreService) {
        this.scoreService = scoreService;
        this.webUI = new WebUI(scoreService);
    }

    @RequestMapping("/diamond-frajkor")
    public String diamond(@RequestParam(value = "command", required = false) String command,
                          @RequestParam(value = "row", required = false) String rowFrom,
                          @RequestParam(value = "column", required = false) String columnFrom,
//                          @RequestParam(value = "rowTo", required = false) String rowTo,
//                          @RequestParam(value = "columnTo", required = false) String columnTo,
                          Model model) {

        Command cmd;

        if (command == null) {
            cmd = ((rowFrom != null) && (columnFrom != null)) ? Command.PLACE : Command.NEW;
        }else{
            cmd = Command.PLACE;
        }

        webUI.processCommand(cmd, rowFrom, columnFrom);
        model.addAttribute("webUI",webUI);
        List<Score> bestScores = scoreService.getBestScores("diamond-frajkor");
        model.addAttribute("scores", bestScores);

        return "diamond-frajkor";
    }


}
