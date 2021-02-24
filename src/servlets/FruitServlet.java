package servlets;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Fruit;
import model.FruitDB;

/**
 * Servlet implementation class FruitServlet
 */
@WebServlet("/FruitServlet")
public class FruitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() throws ServletException {
		FruitDB.insertFruit(new Fruit("orange", "orange"));
		FruitDB.insertFruit(new Fruit("kiwi", "brown"));
		FruitDB.insertFruit(new Fruit("pear", "yellow"));
		FruitDB.insertFruit(new Fruit("apple", "green"));
		FruitDB.insertFruit(new Fruit("apple", "red"));
		FruitDB.insertFruit(new Fruit("banana", "yellow"));
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath()).append(" java version ").append(System.getProperty("java.version"));

		try {

			String color = request.getParameter("color");
			String variety = request.getParameter("variety");
			String action = request.getParameter("action");

			if (color != null && color != "") {
				List<Fruit> filteredFruit = FruitDB.getFruits().stream().filter(fruit -> fruit.getColor().equals(color)).collect(Collectors.toList());
				response.getWriter().append(new Gson().toJson(filteredFruit));
			} else if (variety != null && variety != "") {
				List<Fruit> filteredFruit = FruitDB.getFruits().stream().filter(fruit -> fruit.getVariety().equals(variety)).collect(Collectors.toList());
				response.getWriter().append(new Gson().toJson(filteredFruit));
			} else if (action != null && action != "") {
				getAllFruit(response);
			}
			else {
				notValidInput(response);
			}

		} catch (Exception e) {
			response.getWriter().append("\n[server] -> something went wrong! -> " + e);
		}

	}

	private void notValidInput(HttpServletResponse response) throws IOException {
		responseSettings(response, "text/html");
		response.getWriter().append("\n[server] -> no valid input! \n[server] -> search param look like:\n-> [?action=all] - [?color=green] - [?variety=apple]");
	}

	private void getAllFruit(HttpServletResponse response) throws IOException {
		responseSettings(response, "application/json");
		response.getWriter().append(new Gson().toJson(FruitDB.getFruits()));
	}

	private void responseSettings(HttpServletResponse response, String contentType) {
		response.setContentType(contentType);
		response.setCharacterEncoding("UTF-8");
	}

}
