package pt.fccn.sobre.arquivo.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import pt.fccn.sobre.arquivo.tests.CommonQuestionsTest;
import pt.fccn.sobre.arquivo.tests.ExamplesTest;
import pt.fccn.sobre.arquivo.tests.FooterTest;
import pt.fccn.sobre.arquivo.tests.NavigationTest;
import pt.fccn.sobre.arquivo.tests.NewsTest;
import pt.fccn.sobre.arquivo.tests.PublicationsTest;
import pt.fccn.sobre.arquivo.tests.SearchTest;
import pt.fccn.sobre.arquivo.tests.SiteMapTest;
import pt.fccn.sobre.arquivo.tests.Soft404MessageTest;

	
/**
 * @author João Nobre
 *
 */
@RunWith( Suite.class )
@SuiteClasses( { CommonQuestionsTest.class , ExamplesTest.class , FooterTest.class  , Soft404MessageTest.class , NewsTest.class , SearchTest.class , SiteMapTest.class, PublicationsTest.class , NavigationTest.class  } )
public class TestSuite {

}

