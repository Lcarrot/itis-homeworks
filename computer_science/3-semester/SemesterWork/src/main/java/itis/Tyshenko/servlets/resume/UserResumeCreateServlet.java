package itis.Tyshenko.servlets.resume;

import itis.Tyshenko.dto.ResumeDTO;
import itis.Tyshenko.dto.UserDTO;
import itis.Tyshenko.services.ResumeService;
import itis.Tyshenko.statuses.AdStatus;
import itis.Tyshenko.utility.messages.PreparerMessage;
import itis.Tyshenko.utility.messages.PreparerMessageForResume;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static itis.Tyshenko.utility.PreparedRequestTemplateForEntity.getResumeDtoFromRequest;

@WebServlet(value = "/service/myResume/create")
public class UserResumeCreateServlet extends HttpServlet {

    private ResumeService resumeService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/createJob.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDTO user = (UserDTO) req.getSession().getAttribute("user");
        ResumeDTO resumeDTO = getResumeDtoFromRequest(req);
        PreparerMessage<AdStatus> preparer = new PreparerMessageForResume(resumeDTO);
        if (preparer.checkFields()) {
            resumeService.add(resumeDTO, user.id);
            resp.sendRedirect(req.getContextPath() + "/service/myResume");
        }
        else {
            String error = preparer.getMessage();
            req.setAttribute("error", error);
            req.getRequestDispatcher("/views/createResume.jsp").forward(req, resp);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        resumeService = (ResumeService) config.getServletContext().getAttribute("resumeService");
    }
}
