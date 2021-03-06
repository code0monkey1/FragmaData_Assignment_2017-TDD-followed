package controller;


import model.helperObjects.Critic;
import model.helperObjects.MockFileInfoLoader;
import model.helperObjects.RatedMovie;
import model.helperObjects.ViewedMovie;
import model.primary.customer.CustomerInfo;
import model.primary.movie.MovieInfo;
import model.primary.rating.RatingInfo;
import org.junit.Before;
import org.junit.Test;
import util.FileParsing.FileParser;
import util.InfoLoader.InfoLoader;
import util.mapping.CustomerMapper;
import util.mapping.MovieMapper;
import util.mapping.RatingsMapper;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class StatisticsTest {
    private Statistics statistics;

    @Before
    public void setUp() {

        InfoLoader fileLoader= new MockFileInfoLoader();
        statistics=new Statistics(fileLoader);
    }

    @Test
    public void movies_topNMostViewed() {
        //setup

        List<ViewedMovie> expected = new ArrayList<>();
        expected.add(new ViewedMovie(4, 5));
        expected.add(new ViewedMovie(7, 3));

        assertEquals(expected, statistics.getTopViewedMovies(2));
    }

    private RatingInfo getRatingInfoForQuestion1() {
        FileParser ratingParser = ratingParser();
        RatingsMapper ratingsMapper = new RatingsMapper(ratingParser, 4);


        return new RatingInfo(ratingsMapper.getCustomerIDMovieIDRatingAndTimeMap());
    }

    private FileParser ratingParser() {
        return getFileParser("mockRatingsForMoviesNMostViewed.dat", "::");
    }


    private MovieInfo getMovieInfo() {
        FileParser movieParser = new FileParser("mockMovies.dat", "::");
        MovieMapper movieMapper = new MovieMapper(movieParser, 3);
        return new MovieInfo(movieMapper.getIdMovieMap());
    }


    private FileParser getFileParser(String fileName, String parseToken) {
        FileParser fileParser = new FileParser(fileName, parseToken);
        return fileParser;
    }

    @Test
    public void mostViewedMovies_whenCountExceedsLimit() {

        List<ViewedMovie> expected = new ArrayList<>();
        expected.add(new ViewedMovie(4, 5));
        expected.add(new ViewedMovie(7, 3));
        expected.add(new ViewedMovie(1, 2));

        assertEquals(expected, statistics.getTopViewedMovies(10));

    }


    @Test
    public void ratingList_top3RatedMoviesWithMin2Views() {
        List<RatedMovie> expected = new ArrayList<>();
        expected.add(new RatedMovie(7, 13.0 / 3, 3));
        expected.add(new RatedMovie(1, 8.0 / 2, 2));
        expected.add(new RatedMovie(4, 20.0 / 5, 5));


        assertEquals(expected, statistics.getTopRatedMovies(3, 2));
    }

    @Test
    public void topRatedViewerShipCount_top3MostViewedWithMin2Views() {
        Map<Integer, HashMap> expected = new HashMap<>();

        HashMap<Integer, Integer> firstInternalMap = new HashMap<>();
        firstInternalMap.put(0, 0);
        firstInternalMap.put(1, 2);
        firstInternalMap.put(2, 0);
        expected.put(7, firstInternalMap);

        HashMap<Integer, Integer> secondInternalMap = new HashMap<>();
        secondInternalMap.put(0, 1);
        secondInternalMap.put(1, 0);
        secondInternalMap.put(2, 1);
        expected.put(1, secondInternalMap);

        HashMap<Integer, Integer> thirdInternalMap = new HashMap<>();
        thirdInternalMap.put(0, 0);
        thirdInternalMap.put(1, 2);
        thirdInternalMap.put(2, 2);
        expected.put(4, thirdInternalMap);

        Set<Integer> keys = expected.keySet();
        assertThat(expected, hasKey(1));

        assertThat(expected, is(statistics.getTopRatedMoviesViewership(3, 2)));

//        assertEquals(expected, statistics.getTopRatedMoviesViewership(3, 2));
    }


    @Test
    public void topCritics_GivingMinimumRatingAndHavingMinimum1View() {
        List<Critic> customers = new ArrayList<>();
        customers.add(new Critic(2, 3.0, 1));
        customers.add(new Critic(3, 3.0, 1));
        customers.add(new Critic(6, 3.0, 1));
        customers.add(new Critic(4, 4.0, 1));
        customers.add(new Critic(9, 4.0, 1));
        customers.add(new Critic(10, 4.0, 1));
        customers.add(new Critic(7, 5.0, 1));
        customers.add(new Critic(8, 5.0, 1));
        customers.add(new Critic(1, 5.0, 1));
        customers.add(new Critic(5, 5.0, 1));

        assertEquals(customers, statistics.getTopCritics(10, 1));

        customers.clear();
        customers.add(new Critic(2, 3.0, 1));
        customers.add(new Critic(3, 3.0, 1));
        customers.add(new Critic(6, 3.0, 1));
        customers.add(new Critic(4, 4.0, 1));
        customers.add(new Critic(9, 4.0, 1));
        customers.add(new Critic(10, 4.0, 1));

        assertEquals(customers, statistics.getTopCritics(6, 1));

    }


}