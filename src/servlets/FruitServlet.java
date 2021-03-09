package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Fruit;
import model.FruitDB;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servlet implementation class FruitServlet
 */
@WebServlet("/FruitServlet")
public class FruitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        FruitDB.insertFruit(new Fruit("orange", "orange"));
        FruitDB.insertFruit(new Fruit("kiwi", "brown"));
        FruitDB.insertFruit(new Fruit("pear", "yellow"));
        FruitDB.insertFruit(new Fruit("banana", "yellow"));
        FruitDB.insertFruit(new Fruit("apple", "green"));
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "all" -> getAllFruit(response);
                case "add" -> addFruit(request, response);
                case "delete" -> deleteFruit(request, response);
                case "color" -> getColor(request, response);
                case "variety" -> getVariety(request, response);
                default -> notValidInput(response);
            }
        } else {
            startUp(request,response);
        }
    }

    private void startUp(HttpServletRequest request, HttpServletResponse response) throws IOException {
        responseSettings(response, "text/html");
        response.getWriter().append("server >> Just A Fruit Servlet <br>")
        .append("server >> Served at: ").append(request.getContextPath()).append("<br>")
        .append("server >> java version ").append(System.getProperty("java.version")).append("<br>");
    }

    /**
     *
     * @param response
     * @throws IOException
     */
    private void notValidInput(HttpServletResponse response) throws IOException {
        responseSettings(response, "text/html");
        response.getWriter().append("server >> no valid input!<br>")
                .append("<hr>server >> possible param look like:<br>")
                .append(" >> ?action=all <br>" +
                        " >> ?action=color&value=green <br>" +
                        " >> ?action=variety&value=apple <br>" +
                        " >> ?action=add&variety=apple&color=red<br>" +
                        " >> ?action=delete&variety=apple&color=red<br><hr>");
    }

    /**
     *
     * @param response
     * @throws IOException
     */
    private void getAllFruit(HttpServletResponse response) throws IOException {
        responseSettings(response, "application/json");
        response.getWriter().append(new Gson().toJson(FruitDB.getFruits()));
    }

    /**
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void getColor(HttpServletRequest request, HttpServletResponse response) throws IOException {
        responseSettings(response, "application/json");
        String color = request.getParameter("value");

        if (color != null) {
            List<Fruit> filteredByColor = FruitDB.getFruits().stream()
                    .filter(fruit -> fruit.getColor().equals(color)).collect(Collectors.toList());
            response.getWriter().append(new Gson().toJson(filteredByColor));
        } else {
            notValidInput(response);
        }
    }

    /**
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void getVariety(HttpServletRequest request, HttpServletResponse response) throws IOException {
        responseSettings(response, "application/json");
        String variety = request.getParameter("value");

        if (variety != null) {
            List<Fruit> filteredByVariety = FruitDB.getFruits().stream()
                    .filter(fruit -> fruit.getVariety().equals(variety)).collect(Collectors.toList());
            response.getWriter().append(new Gson().toJson(filteredByVariety));
        } else {
            notValidInput(response);
        }
    }

    /**
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void addFruit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        responseSettings(response, "application/json");
        String variety = request.getParameter("variety");
        String color = request.getParameter("color");

        if (variety == null || color == null) {
            response.getWriter().append(new Gson().toJson("server >> cannot insert null parameter"));
            notValidInput(response);
        } else {
            FruitDB.insertFruit(new Fruit(variety, color));
            response.getWriter().append(new Gson().toJson("server >> inserted fruit " + variety + " " + color + " into FruitDB"));
        }
    }

    /**
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void deleteFruit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        responseSettings(response, "application/json");
        String variety = request.getParameter("variety");
        String color = request.getParameter("color");

        if (variety == null || color == null) {
            response.getWriter().append(new Gson().toJson("server >> cannot delete null parameter"));
            notValidInput(response);
        } else {
            FruitDB.deleteFruit(new Fruit(variety, color));
            response.getWriter().append(new Gson().toJson("server >> deleted fruit " + variety + " " + color + " from FruitDB"));
        }
    }

    /**
     *
     * @param response
     * @param contentType
     */
    private void responseSettings(HttpServletResponse response, String contentType) {
        response.setContentType(contentType);
        response.setCharacterEncoding("UTF-8");
    }

}
