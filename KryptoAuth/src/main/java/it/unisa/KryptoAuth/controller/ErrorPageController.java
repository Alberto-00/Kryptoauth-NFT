package it.unisa.KryptoAuth.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorPageController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model){
        String errorPage = "error"; // default
        String pageTitle = "KryptoAuth - Error ";

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                // handle HTTP 404 Not Found error
                errorPage = "error/404";
                pageTitle += "404";

            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                // handle HTTP 401 Forbidden error
                errorPage = "error/401";
                pageTitle += "401";

            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                // handle HTTP 500 Internal Server error
                errorPage = "error/500";
                pageTitle += "500";
            }
        }
        model.addAttribute("title", pageTitle);
        return errorPage;
    }
}
