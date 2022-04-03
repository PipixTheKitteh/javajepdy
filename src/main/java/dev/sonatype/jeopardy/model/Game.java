package dev.sonatype.jeopardy.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import javax.swing.*;
import java.util.*;

/**
 * An Game is a conceptual series of rounds played back to back.
 * This exists to ensure that clues are not repeated over the rounds.
 *
 * Game also keeps track of scoring for all players
 *
 */

@MongoEntity(collection="games")
public class Game  {

    public ObjectId id;
    public String shortCode;
    public ArrayList<Team> teams;
    public ArrayList<Round> rounds;
    public GameState status=GameState.ready;
    public Date created= new Date();

    public int currentRound = 0;

    public String name;
    public Cell currentCell;


    public Game() {

    }

    public String uuid() {
        return id.toString();
    }
      public Game(String name,Set<String> teamNames,int rounds) {



        this.name=name;
        this.rounds=new ArrayList<>(rounds);
        this.teams=new ArrayList<>(teamNames.size());

        for(String t:teamNames) {
            Team team=new Team(t);
            teams.add(team);
        }

        
    }


    public Set<String> categorySummary() {
        Set<String> cats=new TreeSet<>();
        for(Round r:rounds) {

            for(Row row:r.rows) {
                cats.add(row.category);
            }
        }
        return cats;
    }


    public String leader() {
        Team winner=null;
        for(Team t:teams) {
            if(winner==null || t.comparePoints(winner)>0) winner=t;
        }

        if(winner==null) return "undecided";
        return winner.name;
    }

    public int leaderPoints() {
        Team winner=null;
        for(Team t:teams) {
            if(winner==null || t.comparePoints(winner)>0) winner=t;
        }

        if(winner==null) return 0;
        return winner.score.points;
    }
}

