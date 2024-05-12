package My;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.sendRedirect("index.html");
		response.setIntHeader("refresh", 1);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String city = request.getParameter("city");
		String apiurl = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=49c6079af8e310aebab94f2e68234b05";
		
		URL url = new URL(apiurl);
		HttpURLConnection connection =(HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		InputStream inputstream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputstream);
		
		StringBuilder responsecontent = new StringBuilder();
		
		Scanner sc = new Scanner(reader);
		
		while(sc.hasNext())
		{
			responsecontent.append(sc.nextLine());
		}
		
		sc.close();
		
		Gson gson = new Gson();
		JsonObject jsonobject = gson.fromJson(responsecontent.toString(), JsonObject.class);
		
		long datetime = jsonobject.get("dt").getAsLong()*(1000);
		String date = new Date(datetime).toString();
		
		double temperaturek = jsonobject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperaturec = (int) (temperaturek - 273.15);
		
		int humidity = jsonobject.getAsJsonObject("main").get("humidity").getAsInt();
		
		int windspeed = jsonobject.getAsJsonObject("wind").get("speed").getAsInt();
		
		String weathercondition = jsonobject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		request.setAttribute("date", date);
		request.setAttribute("city", city);
		request.setAttribute("temperature", temperaturec);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windspeed", windspeed);
		request.setAttribute("weathercondition", weathercondition);
		request.setAttribute("weatherData", responsecontent.toString());
		
		connection.disconnect();
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
}
