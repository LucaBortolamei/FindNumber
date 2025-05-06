package com.lucabortolamei.FindNumber;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import com.lucabortolamei.FindNumber.CsvHandler;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

@Controller
public class FindNumberController {
    private int targetNumber;

    public FindNumberController() {
        resetGame();
    }

    private void resetGame() {
        targetNumber = new Random().nextInt(100) + 1;
    }

    @GetMapping("/game")
    public String playGame(@RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "guess", required = false) Integer guess,
                           Model model, HttpSession session) {
        String message = "";

        // Store the player's name in the session
        if (name != null) {
            session.setAttribute("playerName", name);
        }

        // Retrieve the player's name from the session
        String currentPlayer = (String) session.getAttribute("playerName");

        // Initialize or retrieve previous attempts
        if (session.getAttribute("attempts") == null) {
            session.setAttribute("attempts", new ArrayList<String>());
        }
        List<String> previousAttempts = (List<String>) session.getAttribute("attempts");

        if (currentPlayer != null && guess != null) {
            int attempts = previousAttempts.size() + 1; // Track number of guesses

            if (guess < targetNumber) {
                message = "Too small!";
            } else if (guess > targetNumber) {
                message = "Too big!";
            } else {
                message = "You guessed it!" + " Total attempts: " + attempts;
                CsvHandler.savePlayer(currentPlayer, attempts); // Save player to CSV
                session.setAttribute("showResetButton", true);
            }

            // Store attempt history
            previousAttempts.add("Guess: " + guess + " → " + message);
            session.setAttribute("attempts", previousAttempts);
        }

        model.addAttribute("message", message);
        model.addAttribute("playerName", currentPlayer);
        model.addAttribute("attempts", previousAttempts);
        model.addAttribute("showResetButton", session.getAttribute("showResetButton"));

        return "game";
    }

    @GetMapping("/reset")
    public String resetGameHandler(HttpSession session) {
        resetGame();
        session.removeAttribute("playerName"); // Remove name for new game
        session.removeAttribute("attempts"); // Clear previous attempts
        session.removeAttribute("showResetButton"); // Ensure button resets correctly
        return "redirect:/game";
    }

    @GetMapping("/ranking")
    public String showRanking(Model model) {
        List<String[]> players = CsvHandler.loadRanking();
        model.addAttribute("players", players);
        return "ranking";
    }
}
