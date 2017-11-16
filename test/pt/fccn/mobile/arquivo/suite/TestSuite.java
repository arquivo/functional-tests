package pt.fccn.mobile.arquivo.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import pt.fccn.mobile.arquivo.tests.FullTextSearchTest;
import pt.fccn.sobre.arquivo.tests.CommonQuestionsTest;
import pt.fccn.sobre.arquivo.tests.ExamplesTest;
import pt.fccn.sobre.arquivo.tests.FooterTest;
import pt.fccn.sobre.arquivo.tests.NavigationTest;
import pt.fccn.sobre.arquivo.tests.NewsTest;
import pt.fccn.sobre.arquivo.tests.PublicationsTest;
import pt.fccn.sobre.arquivo.tests.SearchTest;
import pt.fccn.sobre.arquivo.tests.SiteMapTest;

/**
 * @author João Nobre
 *
 */
@RunWith( Suite.class )
@SuiteClasses( { FullTextSearchTest.class } )
//TODO AdvancedSearchTest.class, URLSearchTest.class
public class TestSuite {

}