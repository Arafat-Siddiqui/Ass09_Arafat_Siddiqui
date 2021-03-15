import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class MovieTester {
	
	MoviesDao md;
	public MovieTester() {
		// TODO Auto-generated constructor stub
		md=new MoviesDao();
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		MovieTester obj=new MovieTester();
		File file =new File("./src/Movies.txt");
		List<Movies> list=obj.populateMovies(file);
		obj.storeAllMoviesInDb(list);
		//DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yy");
		DateTimeFormatter df=DateTimeFormatter.ofPattern("d/m/yy");
		Movies movie=new Movies(6,"Johnny English",Category.valueOf("Comedy Adventure Animation".replaceAll(" ", "_")),Language.valueOf("English"),Date.valueOf(LocalDate.parse("4/5/12",df)),List.of("Ben Stiller-Chris Rock-David Schwimmer".split("-")),Double.parseDouble("6.8"),Double.parseDouble("53.854") ) ;
		obj.addMovie(movie,list);
		obj.serializeMovies(list, "demo");
		obj.deserializeMovie("demo");
		List<Movies> l2=obj.getMoviesRealeasedInYear(list, 2014);
		List<Movies> l3=obj.getMoviesByActor(l2, "HarryStyles");
	}
	
	List<Movies> populateMovies(File file) throws FileNotFoundException{
		Scanner sc=new Scanner(file);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yy");
		List<Movies> list=new ArrayList<Movies>();
		while(sc.hasNextLine()) {
			String arr[]=sc.nextLine().split(",");
			Movies movie = new Movies(Integer.parseInt(arr[0]),arr[1],Category.valueOf(arr[2].replaceAll(" ", "_").toUpperCase()),Language.valueOf(arr[3].toUpperCase()),Date.valueOf(LocalDate.parse(arr[4],dateTimeFormatter)),List.of(arr[5].split("-")),Double.parseDouble(arr[6]),Double.parseDouble(arr[7]));
			list.add(movie);
		}
		return list;
		
	}
	void addMovie(Movies movie,List<Movies> list) {
		md.addMovie(movie,list);
	}
	Boolean storeAllMoviesInDb(List<Movies> movies) {
		
		return md.storeAllMoviesInDb(movies);
	}
	
	public void serializeMovies(List<Movies> movies, String fileName) {
		File file = new File(fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(movies);
			oos.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Movies> deserializeMovie(String filename){
		List <Movies> movies = new ArrayList<>();
		File file = new File(filename);
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			movies = (List<Movies>) ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movies;
	}
	
	public List<Movies> getMoviesRealeasedInYear(List<Movies> movies,int year){
		List <Movies> movieList = new ArrayList<>();
		for(Movies m:movies) {
			LocalDate date = m.getReleaseDate().toLocalDate();
			if(date.getYear() == year)
				movieList.add(m);
		}
		return movieList;
	}
	
	public List<Movies> getMoviesByActor(List<Movies> movies,String...actorNames){
		List<Movies> movieList =  new ArrayList<>();
		for(Movies m:movies) {
			for(String actor:actorNames) {
				if(m.getCasting().contains(actor)) {
					movieList.add(m);
					break;
				}
			}
		}
		return movieList;
	}
	
	public void updateRatings(Movies movie,double rating,List<Movies> movies) {
		if(movies.contains(movie)) {
			movie.setRating(rating);
			MoviesDao obj;
			md.updateRatings(movie, rating);
			System.out.println("Movie rating updated");
		}
		else {
			System.out.println("Movie not found in the list");
		}
	}
	
	public void updateBusiness(Movies movie, double amount,List<Movies> movies) {
		if(movies.contains(movie)) {
			movie.setTotalBusinessDone(amount);
			md.updateBusiness(movie, amount);
			System.out.println("Business Updated");
		}
		else {
			System.out.println("Movie not found in the list");
		}
	}
	
	public Map<Language,Set<Movies>> businessDone(List<Movies> movies,double amount){
		Set <Movies> movieSet = new TreeSet<>();
		Map <Language,Set<Movies>> movieMap = new HashMap<>();
		for(Movies movie:movies) {
			if(movie.getTotalBusinessDone() > amount) {
				movieSet.add(movie);
				if(movieMap.containsKey(movie.getLanguage())) {
					movieMap.get(movie.getLanguage()).add(movie);
				}
				else {
					movieMap.put(movie.getLanguage(), movieSet);
				}
			}
		}
		
		return movieMap;
	}
	
	

}
