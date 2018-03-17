/*
 * Julia Korcsinszka
 * March 2018
 * 
 * this class contains SPARQL queries that query the 
 * Nobel Laureates dataset called "Nobeldumop.nt"
 */

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;

public class CreateAndQuery {

	public static void main(String[] args) {
		//create model with data from Nobeldump.nt
  	  	Model model = ModelFactory.createDefaultModel();
 		model.read("Nobeldump.nt");
 		// calls methods of the class
		getLaureateFromUK(model);
		getFemaleLaureatesBornAfter49(model);
		listLaureatesByDisciplineAlphabetically(model);
		unitedStatesLaureatesSharingAward(model);
	}

	}
	
	//query1 gets all the laureates that are from the UK.
	public static void getLaureateFromUK(Model model){

		// Create a new query
		// 1) get the resource of the laureates from UK
		// 2) get the name of the laureates
		String queryString = 
		    "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/> " +
		    "PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
		    "SELECT ?name " +
		    "WHERE {" +
		    "      ?laureate dbpedia-owl:birthPlace <http://data.nobelprize.org/resource/country/United_Kingdom> . " +
		    "      ?laureate foaf:name ?name."+ 
		    "}";
		
		Query query = QueryFactory.create(queryString);
		 
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		 
		// Output query results 
		ResultSetFormatter.out(System.out, results, query);
		 
		// Free up resources used running the query
		qe.close();
	}
	
	//query2 gets all the female laureates born after 1949.
	public static void getFemaleLaureatesBornAfter49(Model model){
		
		// Create a new query
		// 1) get only female laureates
		// 2) get the date of birth of those laureates
		// 3) get the names of the laureates
		// 4) filter only those born after 31st December 1949
		String queryString = 
		"PREFIX dbpprop: <http://dbpedia.org/property/>" +
		"PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
		"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" +
		"SELECT ?name " +
		"WHERE {" +
		"      ?laureate foaf:gender \"female\". " +
		"	   ?laureate dbpprop:dateOfBirth ?birthDate." +
		" 	   ?laureate foaf:name ?name." +
		"	   FILTER (?birthDate>\"1949-12-31\"^^xsd:date)." +
		"     }";
			
		Query query = QueryFactory.create(queryString);
				 
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
				 
		// Output query results 
		ResultSetFormatter.out(System.out, results, query);
				 
		// Free up resources used running the query
		qe.close();
	}
	
	//query3 groups Laureates by category of their prize and lists them alphabetically within the group
	public static void listLaureatesByDisciplineAlphabetically(Model model){
		
		// Create a new query
		// 1) get the laureates resource associated with a reward
		// 2) get the category of each award
		// 3) get the names of the laureate resources
		// 4) order the results by category first and then alphabetically
		String queryString = 
		    "PREFIX nobel: <http://data.nobelprize.org/terms/>" +
		    "PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
		    "SELECT ?name ?category " +
		    "WHERE {" +
		    "      ?nameResource nobel:nobelPrize ?award." +
		    "	   ?award nobel:category ?category." +
		    "	   ?nameResource foaf:name ?name." +
		    "      }ORDER BY  ?category asc(UCASE(str(?name)))";
	
		Query query = QueryFactory.create(queryString);
		 
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		
		ResultSet results = qe.execSelect();
		// Output query results 
		ResultSetFormatter.out(System.out, results, query);
		// Free up resources used running the query
		qe.close();	
	}
	
	//queary4 gets the laureates from US that shared their award with somebody
	public static void unitedStatesLaureatesSharingAward(Model model){
		
		// Create a new query
		// 1) get the laureates from the US
		// 2) get the awards of the laureates
		// 3) get the number of people it is shared with
		// 4) get the names of the laureates
		// 5) filter so that only awards shared are displayed
		String queryString = 
		    "PREFIX dbpedia-owl: <http://dbpedia.org/ontology/>" +
		    "PREFIX foaf: <http://xmlns.com/foaf/0.1/>" +
		    "PREFIX nobel: <http://data.nobelprize.org/terms/>" +
		    "SELECT ?name " +
		    "WHERE {" +
		    "      ?laureate dbpedia-owl:birthPlace <http://data.nobelprize.org/resource/country/USA>. " +
		    "	   ?laureate  nobel:laureateAward ?award." +
		    "	   ?award nobel:share ?shared." +
		    "	   ?laureate foaf:name ?name." +
		    "	   FILTER (?shared > \"1\")" +
		    "      }";
	
		Query query = QueryFactory.create(queryString);
		 
		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		 
		// Output query results 
		ResultSetFormatter.out(System.out, results, query);
		 
		// Free up resources used running the query
		qe.close();
	}


}
