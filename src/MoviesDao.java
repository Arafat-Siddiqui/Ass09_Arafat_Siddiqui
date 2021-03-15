import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MoviesDao {
	Connection con;
	public MoviesDao() {
		// TODO Auto-generated constructor stub
		con=DBConnectionUtil.getConnection();
	}
	public boolean storeAllMoviesInDb(List<Movies> movies) {
		PreparedStatement pstmt = null;
		
		for(Movies m : movies) {
			try {
				pstmt = con.prepareStatement("insert into movies values (?,?,?,?,?,?,?)");
				pstmt.setInt(1, m.getMovieId());
				pstmt.setString(2, m.getMovieName());
				pstmt.setString(3, m.getMovieType().name());
				pstmt.setString(4, m.getLanguage().name());
				pstmt.setDate(5, m.getReleaseDate());
				pstmt.setDouble(6, m.getRating());
				pstmt.setDouble(7, m.getTotalBusinessDone());
				pstmt.executeUpdate();
				
				for(String s:m.getCasting()) {
				     pstmt = con.prepareStatement("insert into casting values (?,?)");
				     pstmt.setInt(1,m.getMovieId());
				     pstmt.setString(2, s);
				     pstmt.executeUpdate();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}
	
	void addMovie(Movies movie,List<Movies> list) {
		list.add(movie);
	}
	
	public void updateRatings(Movies movie,double rating) {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement("update movies set rating = ? where movieId = ?");
			pstmt.setDouble(1, rating);
			pstmt.setInt(2, movie.getMovieId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateBusiness(Movies movie, double amount) {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement("update movies set totalBusinessdone = ? where movieId = ?");
			pstmt.setDouble(1, amount);
			pstmt.setInt(2, movie.getMovieId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
