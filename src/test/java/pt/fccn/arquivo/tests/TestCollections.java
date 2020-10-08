package pt.fccn.arquivo.tests;

import static org.junit.Assert.assertTrue;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.util.AppendableErrorsBaseTest;

/**
 * Test collection: test arc proxy, wayback and document servers on both
 * production branches are ok.
 *
 * @author Ivo Branco <ivo.branco@fccn.pt>
 *
 */
public class TestCollections extends AppendableErrorsBaseTest {
	private static final String WAYBACK_PATH = "check";
	private static final String PROTOCOL = "http";
	private static final String PORT = "8082";
	private Collection<String> SERVERS = Arrays.asList("p85.arquivo.pt", "p89.arquivo.pt");

	@Test
	@Retry
	public void testCollectionIA() {
		test(new TestCollectionConfig().collectionId("IA").timestamp("20060706040350").url("http://www.dn.sapo.pt/"));
	}

	@Test
	@Retry
	public void testCollectionRoteiro() {
		test(new TestCollectionConfig().collectionId("Roteiro").timestamp("19961013211156")
				.url("http://www.telepac.pt/filosoft/teste.zip"));
	}

	@Test
	@Retry
	public void testCollectionBN() {
		test(new TestCollectionConfig().collectionId("BN").timestamp("20050130004617")
				.url("http://nautilus.fis.uc.pt/astro/sondagem.php?s=astro1&v=nao"));
	}
	
	@Test
	@Retry
	public void testCollectionDinis() {
		test(new TestCollectionConfig().collectionId("Dinis").timestamp("20090803192654")
				.url("http://www.mediatico.com.pt/25/1.html"));
	}
	
	@Test
	@Retry
	public void testCollectionUL() {
		test(new TestCollectionConfig().collectionId("UL").timestamp("20080303180517")
				.url("http://dba.fc.ul.pt/MestradosDBA/ramos/index.html"));
	}
	
	@Test
	@Retry
	public void testCollectionCEGER() {
		test(new TestCollectionConfig().collectionId("CEGER").timestamp("20061117092548")
				.url("http://www.diadomar.mdn.gov.pt/"));
	}

	@Test
	@Retry
	public void testCollectionBlogsSapo2018() {
		test(new TestCollectionConfig().collectionId("BlogsSapo2018").timestamp("20180815152431")
				.url("https://abemdanacao.blogs.sapo.pt/via-crucis-o-caminhar-da-pessoa-1612858"));
	}

	@Test
	@Retry
	public void testCollectionWeblog() {
		test(new TestCollectionConfig().collectionId("Weblog").timestamp("20121231235959")
				.url("http://www.www.lobito.web.pt/"));
	}

	@Test
	@Retry
	public void testCollectionBlocoEsquerda() {
		test(new TestCollectionConfig().collectionId("BlocoEsquerda").timestamp("20121031235959")
				.url("http://acores.bloco.org/"));
	}

	@Test
	@Retry
	public void testCollectionDEMIST() {
		test(new TestCollectionConfig().collectionId("DEM-IST").timestamp("20061102150547")
				.url("http://www.dem.ist.utl.pt/1.2.cicloEstudosAERO.html"));
	}

	@Test
	@Retry
	public void testCollectionDinisAlves2018() {
		test(new TestCollectionConfig().collectionId("DinisAlves2018").timestamp("20130215005900")
				.url("http://www.portofigueiradafoz.pt/"));
	}
	
	@Test
	@Retry
	public void testCollectionTomba() {
		test(new TestCollectionConfig().collectionId("Tomba").timestamp("20050724165221").url(
				"http://www.arouca.biz/index.php?Itemid=39&date=2005-07-01&extid=20&extmode=view&option=com_extcalendar&recurdate=1118271622"));
	}
	
	@Test
	@Retry
	public void testCollectionRAQ2017() {
		test(new TestCollectionConfig().collectionId("RAQ2017").timestamp("20170831104506").url("http://www.umic.pt/"));
	}

	@Test
	@Retry
	public void testCollectionRAQ2018() {
		test(new TestCollectionConfig().collectionId("RAQ2018").timestamp("20180504071206").url("http://cla.fccn.pt/"));
	}

	@Test
	@Retry
	public void testCollectionRAQ2019() {
		test(new TestCollectionConfig().collectionId("RAQ2019").timestamp("20190315160356")
				.url("http://www.mundonaescola.pt/"));
	}
	
	@Test
	@Retry
	public void testCollectionPATCHING2019() {
		test(new TestCollectionConfig().collectionId("PATCHING2019").timestamp("20140527150350")
				.url("http://i.telegraph.co.uk/telegraph/multimedia/archive/00779/manmohan-singh-460_779953c.jpg"));
	}

	
	// AWP *********
	
	@Test
	@Retry
	public void testCollectionAWP1() {
		test(new TestCollectionConfig().collectionId("AWP1").timestamp("20080306014514")
				.url("http://www.futebol365.pt/competicoes/jornadas.asp?id=3&comp=Chile&epoca=Apertura%202007"));
	}
	
	@Test
	@Retry
	public void testCollectionAWP2() {
		test(new TestCollectionConfig().collectionId("AWP2").timestamp("20080319175807")
				.url("http://oldblogs.sapo.pt/comentar?entry_id=1024648"));
	}

	@Test
	@Retry
	public void testCollectionAWP3() {
		test(new TestCollectionConfig().collectionId("AWP3").timestamp("20081025113615")
				.url("http://www.goblet.pt/Default.aspx?cod_oggetto=REG_PROFILE&ssostatus=ANONYMOUS"));
	}

	@Test
	@Retry
	public void testCollectionAWP4() {
		test(new TestCollectionConfig().collectionId("AWP4").timestamp("20090713124952")
				.url("http://www.blackcrayon.com/people/proudhon/"));
	}

	@Test
	@Retry
	public void testCollectionAWP5() {
		test(new TestCollectionConfig().collectionId("AWP5").timestamp("20091009065728")
				.url("http://www.atheists.org/flash_line/Kansas_church_liable_in_Marine_funeral_protest"));
	}
	
	@Test
	@Retry
	public void testCollectionAWP6() {
		test(new TestCollectionConfig().collectionId("AWP6").timestamp("20100204073930")
				.url("http://valedosousa.blogs.sapo.pt/598901.html"));
	}
	
	@Test
	@Retry
	public void testCollectionAWP7() {
		test(new TestCollectionConfig().collectionId("AWP7").timestamp("20100604204455")
				.url("http://www.embest.pt/eng/rssfeed"));
	}

	@Test
	@Retry
	public void testCollectionAWP8() {
		test(new TestCollectionConfig().collectionId("AWP8").timestamp("20100804063841").url("http://www.fccn.pt/"));
	}
	
	@Test
	@Retry
	public void testCollectionAWP9() {
		test(new TestCollectionConfig().collectionId("AWP9").timestamp("20110121161516")
				.url("http://www.arquitectura.pt/forum/blogs/"));
	}

	@Test
	@Retry
	public void testCollectionAWP10() {
		test(new TestCollectionConfig().collectionId("AWP10").timestamp("20110525201803")
				.url("http://en.eurobilltracker.com/profile/?region=1322"));
	}
	
	@Test
	@Retry
	public void testCollectionAWP11() {
		test(new TestCollectionConfig().collectionId("AWP11").timestamp("20110704044154")
				.url("http://www.agencia.valpi.pt/promocoesPackEscolhido.aspx?promocao=214"));
	}
	
	@Test
	@Retry
	public void testCollectionAWP12() {
		test(new TestCollectionConfig().collectionId("AWP12").timestamp("20120120202904")
				.url("http://camper.eclassificados.com.pt/vale-de-mendiz.htm"));
	}
	
	@Test
	@Retry
	public void testCollectionAWP15() {
		test(new TestCollectionConfig().collectionId("AWP15").timestamp("20131111125215")
				.url("http://www.anunico.com.pt/madeira/Achada+do+Marques/"));
	}
	
	@Test
	@Retry
	public void testCollectionAWP16() {
		test(new TestCollectionConfig().collectionId("AWP16").timestamp("20141001202612")
				.url("http://1001cartasdeamor.pt/envia_carta.asp?id_carta=7698"));
	}
	
	@Test
	@Retry
	public void testCollectionAWP17() {
		test(new TestCollectionConfig().collectionId("AWP17").timestamp("20150413075000")
				.url("http://cabeco-de-sao-tome.blidoo.pt/"));
	}
	
	@Test
	@Retry
	public void testCollectionAWP18() {
		test(new TestCollectionConfig().collectionId("AWP18").timestamp("20150908100324")
				.url("http://moitomotos.lda.pt/img-sys/headerbg.jpg"));
	}
	
	@Test
	@Retry
	public void testCollectionAWP19() {
		test(new TestCollectionConfig().collectionId("AWP19").timestamp("20151112143638")
				.url("http://equilibrium.co.ao/index.php/referencias/itemlist?format=feed&moduleID=217"));
	}
	
	@Test
	@Retry
	public void testCollectionAWP20() {
		test(new TestCollectionConfig().collectionId("AWP20").timestamp("20160220113203")
				.url("http://mocidadesgemil-ac.maiadigital.pt/arrowLeft.gif"));
	}

	@Test
	@Retry
	public void testCollectionAWP21() {
		test(new TestCollectionConfig().collectionId("AWP21").timestamp("20160614104852")
				.url("http://www.risadas.pt/img/videos/90/epic_cat_gymnast_imitates_fireman_afv_thumb_18990.jpg"));
	}

	@Test
	@Retry
	public void testCollectionAWP22() {
		test(new TestCollectionConfig().collectionId("AWP22").timestamp("20161103115623")
				.url("http://los-amigos-beach-club-hotel-mijas-costa.booked.com.pt/robots.txt"));
	}
	
	@Test
	@Retry
	public void testCollectionAWP23() {
		test(new TestCollectionConfig().collectionId("AWP23").timestamp("20170223165940")
				.url("https://jornadas.fccn.pt/"));
	}

	@Test
	@Retry
	public void testCollectionAWP24() {
		test(new TestCollectionConfig().collectionId("AWP24").timestamp("20170809032837").url("https://fccn.pt/"));
	}

	@Test
	@Retry
	public void testCollectionAWP25() {
		test(new TestCollectionConfig().collectionId("AWP25").timestamp("20171213225847").url("https://www.fccn.pt/"));
	}
	
	@Test
	@Retry
	public void testCollectionAWP26() {
		test(new TestCollectionConfig().collectionId("AWP26").timestamp("20180415222152").url("https://www.fccn.pt/"));
	}

	@Test
	@Retry
	public void testCollectionAWP27() {
		test(new TestCollectionConfig().collectionId("AWP27").timestamp("20180720124453").url("https://www.fccn.pt/"));
	}

	@Test
	@Retry
	public void testCollectionAWP28() {
		test(new TestCollectionConfig().collectionId("AWP28").timestamp("20181015061643").url("https://www.fccn.pt/"));
	}
	
	// EAWP *********

	@Test
	@Retry
	public void testCollectionEAWP1() {
		test(new TestCollectionConfig().collectionId("EAWP1").timestamp("20110520163623")
				.url("http://crawler.archive.org/"));
	}
	
	@Test
	@Retry
	public void testCollectionEAWP2() {
		test(new TestCollectionConfig().collectionId("EAWP2").timestamp("20110622013138")
				.url("http://www.dgaep.gov.pt/index.cfm?OBJID=91f17207-d63e-4f78-a525-4e8140f46f49&ID=742"));
	}
	
	@Test
	@Retry
	public void testCollectionEAWP5() {
		test(new TestCollectionConfig().collectionId("EAWP5").timestamp("20141023180949").url(
				"http://guarda1.prof2000.pt/moodle/help.php?module=moodle&file=courseavailability.html&forcelang=en_utf8"));
	}
	
	@Test
	@Retry
	public void testCollectionEAWP6() {
		test(new TestCollectionConfig().collectionId("EAWP6").timestamp("20141126050845")
				.url("http://motorcycle.e-mp3s.eu/313514-motorcycle-as-the-rush-comes-ga.html"));
	}

	@Test
	@Retry
	public void testCollectionEAWP7() {
		test(new TestCollectionConfig().collectionId("EAWP7").timestamp("20150922131415").url(
				"http://www.legislativas2015.pt/2015/09/04/barometro-eurosondagem-setembro-2015-vantagem-do-ps-reduz-para-1/"));
	}
	
	@Test
	@Retry
	public void testCollectionEAWP8() {
		test(new TestCollectionConfig().collectionId("EAWP8").timestamp("20160107165730")
				.url("http://efeito.net/p.registrarfccn"));
	}
	
	@Test
	@Retry
	public void testCollectionEAWP9() {
		test(new TestCollectionConfig().collectionId("EAWP9").timestamp("20160121105804")
				.url("http://opanorama.pt/wp-content/uploads/2015/07/maria-de-belem1-e1444315722508-250x167.jpg"));
	}
	
	@Test
	@Retry
	public void testCollectionEAWP10() {
		test(new TestCollectionConfig().collectionId("EAWP10").timestamp("20160314123045")
				.url("http://iland.boku.ac.at/iLand"));
	}

	@Test
	@Retry
	public void testCollectionEAWP10_2() {
		test(new TestCollectionConfig().collectionId("EAWP10-2").timestamp("20160421110549")
				.url("https://nano4water.vito.be/Pages/home.aspx"));
	}

	@Test
	@Retry
	public void testCollectionEAWP11() {
		test(new TestCollectionConfig().collectionId("EAWP11").timestamp("20160516012203")
				.url("http://www.uni-klu.ac.at/main/inhalt/1.htm"));
	}

	@Test
	@Retry
	public void testCollectionEAWP11_2() {
		test(new TestCollectionConfig().collectionId("EAWP11-2").timestamp("20160516190154")
				.url("http://www.niederschlagsradar.de/"));
	}

	@Test
	@Retry
	public void testCollectionEAWP12() {
		test(new TestCollectionConfig().collectionId("EAWP12").timestamp("20160817135523")
				.url("http://www.inesctec.pt/window_icon.gif"));
	}

	@Test
	@Retry
	public void testCollectionEAWP13() {
		test(new TestCollectionConfig().collectionId("EAWP13").timestamp("20161118131923")
				.url("https://www.wmo.int/pages/prog/arep/gaw/ozone/nrt_plots.html"));
	}
	
	@Test
	@Retry
	public void testCollectionEAWP14() {
		test(new TestCollectionConfig().collectionId("EAWP14").timestamp("20010106003531")
				.url("http://www.diariodigital.com/artigo.asp?cod_artigo=86013"));
	}
	
	@Test
	@Retry
	public void testCollectionEAWP15() {
		test(new TestCollectionConfig().collectionId("EAWP15").timestamp("20170620204254").url("https://www.fccn.pt/"));
	}

	@Test
	@Retry
	public void testCollectionEAWP16() {
		test(new TestCollectionConfig().collectionId("EAWP16").timestamp("20170927112019")
				.url("http://blogs.sapo.pt/"));
	}

	@Test
	@Retry
	public void testCollectionEAWP17() {
		test(new TestCollectionConfig().collectionId("EAWP17").timestamp("20171010134435").url("http://www.sapo.pt/"));
	}

	@Test
	@Retry
	public void testCollectionEAWP18() {
		test(new TestCollectionConfig().collectionId("EAWP18").timestamp("20171003132227")
				.url("https://usabilidade.gov.pt/sobre-o-portal"));
	}
	
	@Test
	@Retry
	public void testCollectionEAWP19() {
		test(new TestCollectionConfig().collectionId("EAWP19").timestamp("20181210071242")
				.url("https://www.avatrade.pt/"));
	}

	@Test
	@Retry
	public void testCollectionEAWP20() {
		test(new TestCollectionConfig().collectionId("EAWP20").timestamp("20181205181352").url(
				"https://www.independent.co.uk/voices/michael-flynn-avoids-prison-sentence-mueller-investigation-russia-collusion-donald-trump-american-a8668351.html"));
	}

	@Test
	@Retry
	public void testCollectionEAWP21() {
		test(new TestCollectionConfig().collectionId("EAWP21").timestamp("20190314163830")
				.url("https://commons.wikimedia.org/wiki/Category:World_War_I"));
	}
	
	@Test
	@Retry
	public void testCollectionEAWP23() {
		test(new TestCollectionConfig().collectionId("EAWP23").timestamp("20190522205517")
				.url("https://www.fccn.pt/"));
	}
	
	@Test
	@Retry
	public void testCollectionEAWP27() {
		test(new TestCollectionConfig().collectionId("EAWP27").timestamp("20191105154542")
				.url("https://www.tau.ac.il/"));
	}
	
	// FAWP *********
	
	@Test
	@Retry
	public void testCollectionFAWP1() {
		test(new TestCollectionConfig().collectionId("FAWP1").timestamp("20100519144148")
				.url("http://www.jornalnordeste.com/func/vote.asp?idEdicao=316&id=14104&idSeccao=2836&Action=noticia"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP2() {
		test(new TestCollectionConfig().collectionId("FAWP2").timestamp("20100829220123")
				.url("http://www.vidaeconomica.pt/gen.pl?p=stories&op=view&fokey=ve.stories/47937"));
	}

	@Test
	@Retry
	public void testCollectionFAWP3() {

		test(new TestCollectionConfig().collectionId("FAWP3").timestamp("20101107040207").url(
				"http://www.sabado.pt/Multimedia/Videos/Reportagem/Vazio-(NAO-GRAVAR)-(3)/Vazio-(7).aspx?year=2009&month=8"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP4() {
		test(new TestCollectionConfig().collectionId("FAWP4").timestamp("20110113160259").url("http://publico.pt/"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP5() {
		test(new TestCollectionConfig().collectionId("FAWP5").timestamp("20110401225449")
				.url("http://www.meiosepublicidade.pt/2010/11/12/rtp-e-myway-lancam-radio-operacao-triunfo/"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP6() {
		test(new TestCollectionConfig().collectionId("FAWP6").timestamp("20110906200326").url(
				"http://www.diariodosacores.pt/index.php?option=com_content&view=article&id=10422:ciganos-e-holocausto&catid=30:opiniao"));
	}

	@Test
	@Retry
	public void testCollectionFAWP7() {
		test(new TestCollectionConfig().collectionId("FAWP7").timestamp("20111024033639")
				.url("http://www.entrevilas.com/noticia_access.asp?idEdicao=188&id=5085&idSeccao=1418&Action=noticia"));
	}

	@Test
	@Retry
	public void testCollectionFAWP8() {
		test(new TestCollectionConfig().collectionId("FAWP8").timestamp("20120225160248")
				.url("http://expressodooriente.com/"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP10() {
		test(new TestCollectionConfig().collectionId("FAWP10").timestamp("20120918165941")
				.url("http://www.sintradesportivo.blogspot.pt/2008/03/pool-liga-brunswick-portugal-finais-no.html"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP11() {
		test(new TestCollectionConfig().collectionId("FAWP11").timestamp("20121026150921")
				.url("http://portalivros.wordpress.com/"));
	}

	@Test
	@Retry
	public void testCollectionFAWP12() {
		test(new TestCollectionConfig().collectionId("FAWP12").timestamp("20130111021452")
				.url("http://www.avozdeermesinde.com/forum/message.asp?IdMsg=1254&idForum=1"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP14() {
		test(new TestCollectionConfig().collectionId("FAWP14").timestamp("20130801191854")
				.url("http://www.jornaldamealhada.com/multimedia/show.aspx?idioma=pt&idcont=1656"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP15() {
		test(new TestCollectionConfig().collectionId("FAWP15").timestamp("20131021164656")
				.url("http://properties.theportugalnews.com/"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP17() {
		test(new TestCollectionConfig().collectionId("FAWP17").timestamp("20140517190551")
				.url("http://www.publico.pt/desporto/"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP18() {
		test(new TestCollectionConfig().collectionId("FAWP18").timestamp("20140811220257").url(
				"http://jornalodiabo.blogspot.pt/2012/09/casa-de-salazar-destruida-e-abandonada.html?showComment=1346918299590"));
	}

	@Test
	@Retry
	public void testCollectionFAWP19() {
		test(new TestCollectionConfig().collectionId("FAWP19").timestamp("20141003194744").url(
				"http://lifestyle.publico.pt/noticias/339887_william-e-kate-vao-processar-fotografo-que-anda-a-perseguir-o-filho"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP20() {
		test(new TestCollectionConfig().collectionId("FAWP20").timestamp("20150106180216tf_")
				.url("http://www.abola.pt/"));
	}

	@Test
	@Retry
	public void testCollectionFAWP21() {
		test(new TestCollectionConfig().collectionId("FAWP21").timestamp("20150502170218tf_")
				.url("http://www.abola.pt/"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP22() {
		test(new TestCollectionConfig().collectionId("FAWP22").timestamp("20150826170520").url(
				"https://scontent.cdninstagram.com/hphotos-xfa1/t51.2885-15/s320x320/e35/11881586_755635301225487_1974167864_n.jpg"));
	}

	@Test
	@Retry
	public void testCollectionFAWP23() {
		test(new TestCollectionConfig().collectionId("FAWP23").timestamp("20151002170346")
				.url("http://www.radioonline.com.pt/regiao/acores/"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP24() {
		test(new TestCollectionConfig().collectionId("FAWP24").timestamp("20160206150936im_")
				.url("http://correiodafeira.pt/jornalcf/wp-content/uploads/2013/01/Imagem-21.jpg"));
	}

	@Test
	@Retry
	public void testCollectionFAWP25() {
		test(new TestCollectionConfig().collectionId("FAWP25").timestamp("20160404194712im_").url(
				"http://correiodafeira.pt/jornalcf/wp-content/themes/city-desk/timthumb.php?src=http%3A%2F%2Fcorreiodafeira.pt%2Fjornalcf%2Fwp-content%2Fuploads%2F2016%2F03%2Fmusica-portuguesa5.jpg&q=90&w=93&h=50&zc=1"));
	}

	@Test
	@Retry
	public void testCollectionFAWP26() {
		test(new TestCollectionConfig().collectionId("FAWP26").timestamp("20160809194337im_").url(
				"http://correiodafeira.pt/jornalcf/wp-content/uploads/2016/07/conceicao-alvim-e-miguel-ferraz2-150x150.jpg"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP27() {
		test(new TestCollectionConfig().collectionId("FAWP27").timestamp("20161217054847").url(
				"http://correiodafeira.pt/jornalcf/wp-content/uploads/2016/07/conceicao-alvim-e-miguel-ferraz6-150x150.jpg"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP28() {
		test(new TestCollectionConfig().collectionId("FAWP28").timestamp("20170128032423")
				.url("https://www.airbnb.co.ve/rooms/16908431"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP29() {
		test(new TestCollectionConfig().collectionId("FAWP29").timestamp("20170401174131").url("https://www.fccn.pt/"));
	}

	@Test
	@Retry
	public void testCollectionFAWP30() {
		test(new TestCollectionConfig().collectionId("FAWP30").timestamp("20170701174403").url("https://www.fccn.pt/"));
	}

	@Test
	@Retry
	public void testCollectionFAWP31() {
		test(new TestCollectionConfig().collectionId("FAWP31").timestamp("20171001174419").url("https://www.fccn.pt/"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP32() {
		test(new TestCollectionConfig().collectionId("FAWP32").timestamp("20180327035850")
				.url("https://www.fccn.pt/wp-content/uploads/2018/03/noticia_tsf.jpg"));
	}

	@Test
	@Retry
	public void testCollectionFAWP33() {
		test(new TestCollectionConfig().collectionId("FAWP33").timestamp("20180401232524")
				.url("https://www.fccn.pt/wp-content/uploads/2018/02/candidaturas_premiosarquivo_3.jpg"));
	}

	@Test
	@Retry
	public void testCollectionFAWP34() {
		test(new TestCollectionConfig().collectionId("FAWP34").timestamp("20180906204204")
				.url("https://www.fccn.pt/wp-content/uploads/2018/09/image001-300x179.jpg"));
	}

	@Test
	@Retry
	public void testCollectionFAWP35() {
		test(new TestCollectionConfig().collectionId("FAWP35").timestamp("20181218221034").url(
				"https://www.fccn.pt/wp-content/uploads/2018/12/xRCAAP-2-768x512.jpg.pagespeed.ic.1fY2yHwHIs.jpg"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP36() {
		test(new TestCollectionConfig().collectionId("FAWP35").timestamp("20190115214628").url(
				"https://www.diariandorra.ad/NUCLEO/jquery/images/ui-bg_flat_75_ffffff_40x100.png"));
	}
	
	@Test
	@Retry
	public void testCollectionFAWP37() {
		test(new TestCollectionConfig().collectionId("FAWP35").timestamp("20190622090359").url(
				"https://blogs.uaeu.ac.ae/"));
	}
	
	//  *******

	private void test(TestCollectionConfig config) {
		for (String server : SERVERS) {
			String testUrl = config.getTestURL(server);
			String collectionId = config.getCollectionId();

//			System.out.println(String.format("Begin checking url: %s with full configuration as: %s", testUrl,
//					ReflectionToStringBuilder.toString(config, ToStringStyle.MULTI_LINE_STYLE)));

			appendError(() -> assertTrue(
					String.format("Check current wayback page url returns 200 - %s - %s", collectionId, testUrl),
					doesURLExist(testUrl)));
		}
	}

	public static boolean doesURLExist(String testUrl) {
		try {
			URL url = new URL(testUrl);

			// We want to check the current URL
			HttpURLConnection.setFollowRedirects(false);

			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

			// We don't need to get data
			httpURLConnection.setRequestMethod("HEAD");

			// Some websites don't like programmatic access so pretend to be a browser
//			httpURLConnection.setRequestProperty("User-Agent",
//					"Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
			int responseCode = httpURLConnection.getResponseCode();

			// We only accept response code 200
			return responseCode == HttpURLConnection.HTTP_OK;
		} catch (Exception e) {
			throw new RuntimeException("Error connecting to URL: " + testUrl, e);
		}
	}

	private static class TestCollectionConfig {

		private String collectionId;
		private String timestamp;
		private String url;

		public String getTestURL(String server) {
			return PROTOCOL + "://" + server + ":" + PORT + "/" + WAYBACK_PATH + "/" + timestamp + "/" + url;
		}

		public TestCollectionConfig collectionId(String collectionId) {
			this.collectionId = collectionId;
			return this;
		}

		public TestCollectionConfig timestamp(String timestamp) {
			this.timestamp = timestamp;
			return this;
		}

		public TestCollectionConfig url(String url) {
			this.url = url;
			return this;
		}

		public String getCollectionId() {
			return collectionId;
		}

	}
}
