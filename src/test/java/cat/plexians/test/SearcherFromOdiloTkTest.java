package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import cat.plexians.utils.*;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearcherFromOdiloTkTest extends BaseWebDriver {

    @Test
    public void initialTesT() throws InterruptedException, IOException {

        String isbn = "9788410305182";

        String pathForDownloads = File.separator + "Volumes" + File.separator + "02_2TB" + File.separator + "odilotk_searches" + File.separator;

        String searchParams = File.separator + "results?limit=24&offset=0&query=allfields_txt:" + isbn + "&order=relevance:desc";

        // Preparem els directoris
        parsingUtils.directoryCreation(pathForDownloads);

        // La teva llista d'URLs completa
        List<String> urls = Arrays.asList(
                "https://colegiobrasil.odilo.us", "https://colegiolatam.odilo.es", "https://academic.odilo.us",
                "https://biblioteca.odilo.us", "https://business-school.odilo.es", "https://business-school.odilo.us",
                "https://corporate.odilo.es", "https://corporativobrasil.odilo.us", "https://library.odilo.us",
                "https://colegio.odilo.es", "https://university.odilo.es", "https://university.odilo.us",
                "https://workplace.odilo.us", "https://aedrh.unlimitedlearning.io", "https://allfunds.unlimitedlearning.io",
                "https://academy.awwg.com", "https://academiadecienciasytecnologia.odilotk.es", "https://demo-academics.odilo.us",
                "https://agoda.odilo.io", "https://americanschoolofpachuca.odilotk.es", "https://localotk-app.odilotk.es",
                "https://biblioteca.asambleamurcia.es", "https://asambleaex.odilotk.es", "https://ascendger.odilo.us",
                "https://aim.odilo.us", "https://www.theologicalebooks.org", "https://auladigital.comunidad.madrid",
                "https://indaloteka.odilotk.es", "https://barmm.unlimitedlearning.io", "https://bibliotecasdaenergia.odilo.us",
                "https://bpdigital.bnp.gob.pe", "https://www.bpdigital.cl", "https://buap.unlimitedlearning.io",
                "https://bibliotecaemarch.odilotk.es", "https://capacitaciones.caf.com", "https://bi-blioteca.odilotk.es",
                "https://micampus.pichincha.com", "https://campusbancopichinchaespana.unlimitedlearning.io", "https://masconectadosbch.odilotk.es",
                "https://biblion.odilo.us", "https://udima.odilo.es", "https://www.biblioclick.cl",
                "https://bibliotecacortsvalencianes.odilotk.es", "https://bibliotecadigital.bibliodrogas.gob.cl", "https://bibliotecadigitalercilianarvaez.odilo.es",
                "https://bdescolar.mineduc.cl", "https://bibliotecadigital.huechuraba.cl", "https://jccm.odilotk.es",
                "https://mehextranet.odilotk.es", "https://meh.odilotk.es", "https://ga-p.odilo.es",
                "https://icam.odilotk.es", "https://bibliotecaparquedigital.rj.gov.br", "https://bibliotecapais.ceibal.edu.uy",
                "https://bibliotecapais.ceibal.edu.uy", "https://www.bibliotecadigitalvitacura.cl", "https://descubrelo.riecken.org",
                "https://catalogobibliotecas.cartagena.es", "https://bipi.unlimitedlearning.io", "https://masterandminds.unlimitedlearning.io",
                "https://bcls.odilo.us", "https://bregalmilestone.unlimitedlearning.io", "https://brocktonpubliclibrary.odilo.us",
                "https://conocimiento.broseta.com", "https://academy.buff.com", "https://school.odilo.es",
                "https://sanagustin.unlimitedlearning.io", "https://agoravirtual.bibliotecaceu.es", "https://cfcst.odilo.us",
                "https://cftestatales.odilotk.es", "https://cnci.unlimitedlearning.io", "https://bibliotecadigital.coit.es",
                "https://copib.odilotk.es", "https://calasancio.unlimitedlearning.io", "https://campus.aitanaoncloud.com",
                "https://biblioteca.ebiblio.cat", "https://elib.ctu.edu.ph", "https://cca.unlimitedlearning.io",
                "https://cesurgd.unlimitedlearning.io", "https://cesur.unlimitedlearning.io", "https://champagnatperu.odilotk.es",
                "https://cap.unlimitedlearning.io", "https://cloudlearning.odilo.us", "https://biblioteca.nsdelpilar.edu.pe",
                "https://bibliotecacopc.odilotk.es", "https://copmadrid.odilo.es", "https://biblioteca.colegiosancayetano.com",
                "https://cometa.unlimitedlearning.io", "https://bibliotecadigital.comfaboy.com.co", "https://bibliotecadigital.comfachoco.com.co",
                "https://bibliotecadigital.comfaoriente.com", "https://biblio.cyldigital.es", "https://bibliotecaconferenciaepiscopal.odilo.es",
                "https://congresoperu.odilo.us", "https://retoleermas.odilo.us", "https://culturallascondes.odilotk.es",
                "https://cecar.unlimitedlearning.io", "https://biblioccyl.odilo.es", "https://credicorpcampus.unlimitedlearning.io",
                "https://conocimientoaunclic.ccb.org.co", "https://demo-dglab.unlimitedlearning.io", "https://dandemutande.unlimitedlearning.io",
                "https://demo.odilo.es", "https://demo-moe.odilo.us", "https://demo.odilo.us",
                "https://collections.odilo.us", "https://indyreads.libraries.nsw.gov.au", "https://ebiblio.dacoruna.gal",
                "https://dipbadajoz.odilotk.es", "https://dhg.unlimitedlearning.io", "https://eleo.mecd.gob.es",
                "https://eserp.odilotk.es", "https://eude.odilotk.es", "https://www.ebiblioandorra.ad",
                "https://econet.unlimitedlearning.io", "https://suscripciones.paidotribopremium.com", "https://edubiblio.educacio.gencat.cat",
                "https://eduteca.biblioescolaib.cat", "https://elebiblioteca.odilotk.es", "https://universidadbibliotecadigital.empresaspolar.com",
                "https://endeavor.unlimitedlearning.io", "https://executive.odilo.us", "https://digitalizacion.educacion.navarra.es",
                "https://melies.odilo.us", "https://fsanpablo.unlimitedlearning.io", "https://firstmutual.unlimitedlearning.io",
                "https://flo.odilo.us", "https://formacionydesarrollo.odilo.es", "https://formacionen5g.unlimitedlearning.io",
                "https://fvrl.odilo.us", "https://sdespierto.odilotk.es", "https://ucompensar.unlimitedlearning.io",
                "https://bibliotecasanmartin.odilotk.es", "https://areandina.odilotk.es", "https://hditeca.unlimitedlearning.io",
                "https://bibliotecadigital.gacetajuridica.com.pe", "https://goldenrey.odilo.us", "https://googlesg.unlimitedlearning.io",
                "https://gecoas.odilotk.es", "https://redaprende.unlimitedlearning.io", "https://universidadnichos.com",
                "https://grupopichincha.unlimitedlearning.io", "https://grupogransolar.unlimitedlearning.io", "https://htba.odilo.es",
                "https://hospes.unlimitedlearning.io", "https://icam.odilotk.es", "https://school.odilo.es",
                "https://inap.odilo.es", "https://iqacademy.fundacioniqtek.org", "https://iteso.odilotk.es",
                "https://biblioiberdrola.odilotk.es", "https://learningmx.unlimitedlearning.io", "https://learning.idilia.es",
                "https://indyreads.libraries.nsw.gov.au", "https://innovaschools.odilotk.es", "https://centrodocaya.odilotk.es",
                "https://ipss.odilo.us", "https://ismarina.odilotk.es", "https://interbooks-corporate.odilo.es",
                "https://interbooks-university.odilo.es", "https://jme-vc.odilotk.es", "https://johnjermain.odilo.us",
                "https://kenailibrary.odilo.us", "https://kiboventures.unlimitedlearning.io", "https://landmarhotels.unlimitedlearning.io",
                "https://leocyl.educa.jcyl.es", "https://librarium.educarex.es", "https://lintac.odilotk.es",
                "https://loom.unlimitedlearning.io", "https://rededucativaprodec.odilo.us", "https://mitbiology.odilotk.es",
                "https://mscmarinduque.odilo.us", "https://ulearn.mtn.com", "https://madread.educa.madrid.org",
                "https://pnm.odilo.us", "https://docentesmac.odilo.us", "https://memoriadigital-formacion.odilo.es",
                "https://learnhub.unlimitedlearning.io", "https://odilo.exar.com.ar", "https://moe-colombia.odilotk.es",
                "https://bibliotecamineducec.odilotk.es", "https://ministerioeducacion.odilo.us", "https://mutualidad.unlimitedlearning.io",
                "https://nasafcu.unlimitedlearning.io", "https://nemsu.odilo.us", "https://library.bnr.rw",
                "https://nlp.odilo.us", "https://formacionprofesional.odilo.es", "https://keycompetences.unlimitedlearning.io",
                "https://textbookevolution.unlimitedlearning.io", "https://test-developer-atlas.odilotk.es", "https://testotk-atlas.odilotk.es",
                "https://test-developer-namek.odilotk.es", "https://testotk-namek.odilotk.es", "https://test-developer.odilotk.es",
                "https://testotk-auto.odilotk.es", "https://testotk-waldo.odilotk.es", "https://testotk-waldo2.odilotk.es",
                "https://www.ebiblioandorra.ad", "https://executive.unlimitedlearning.io", "https://cebe.odilo.es",
                "https://odiloib.unlimitedlearning.io", "https://languagelearning.unlimitedlearning.io", "https://lifelong.odilo.es",
                "https://literacy.unlimitedlearning.io", "https://odilofy.odilo.es", "https://odilolab.unlimitedlearning.io",
                "https://odisea.odilotk.es", "https://prima.odilo.us", "https://parlamentcatalunya.odilo.cat",
                "https://parlamentodeandalucia.odilo.es", "https://petrobras.unlimitedlearning.io", "https://maristasamericacentral.unlimitedlearning.io",
                "https://testotk.odilotk.es", "https://pottencia.unlimitedlearning.io", "https://pmilearning.unlimitedlearning.io",
                "https://puntosvuela.unlimitedlearning.io", "https://qatarnl.unlimitedlearning.io", "https://rmbco.odilotk.es",
                "https://rfhl.odilo.us", "https://nymasoniclibrary.odilo.us", "https://bibliotecasebraemg.unlimitedlearning.io",
                "https://secphilippines.unlimitedlearning.io", "https://leiaparana.odilo.us", "https://seedtag.unlimitedlearning.io",
                "https://bibliosek.odilotk.es", "https://cachola.senac.br", "https://shs-adc.unlimitedlearning.io",
                "https://skoda.odilotk.es", "https://sprl.odilo.us", "https://steam.unlimitedlearning.io",
                "https://salesianosbizkaia.unlimitedlearning.io", "https://sanbenildorizal.unlimitedlearning.io", "https://sanluis.odilo.us",
                "https://smcl.odilo.us", "https://educamedellin.unlimitedlearning.io", "https://sector.odilotk.es",
                "https://sictel.odilo.us", "https://sika.unlimitedlearning.io", "https://southernchristiancollege.odilo.es",
                "https://southernleytestateu.unlimitedlearning.io", "https://sustainabilityd-i.odilo.es", "https://swanlaab.unlimitedlearning.io",
                "https://kit-teamurcia.odilo.es", "https://teachertraining.unlimitedlearning.io", "https://tslvirtualacademy.unlimitedlearning.io",
                "https://teatroteca.teatro.es", "https://cloudexacademy.unlimitedlearning.io", "https://test-otk4opl-dev.odilotk.es",
                "https://testotk-java17.odilotk.es", "https://thecore.odilo.es", "https://ucav.odilotk.es",
                "https://uceva.odilo.us", "https://dibridigital.ucsh.cl", "https://odilo.ucuenca.edu.ec",
                "https://odilo.udit.es", "https://uteca.unemi.edu.ec", "https://bibliotecauniacc.odilotk.es",
                "https://catalogobiblioteca.unir.net", "https://vlib.unired.edu.co", "https://ukraine.unlimitedlearning.io",
                "https://uts.odilo.us", "https://bibunimonserrate.odilotk.es", "https://elearning.univalle.edu",
                "https://colecciondigital.uda.cl", "https://campusbiblioteca.uautonoma.cl", "https://campusautonoma.unlimitedlearning.io",
                "https://bibliotecacun.odilotk.es", "https://library.ucatolica.edu.co", "https://yachay.ucv.edu.pe",
                "https://bibliotecacomplutense.odilotk.es", "https://universidadcorporativanauterra.odilo.es", "https://bibliotecadigital.ufv.es",
                "https://uis.odilotk.es", "https://unia.odilo.es", "https://ufiloyola.unlimitedlearning.io",
                "https://bibliotecaupn.odilo.us", "https://bibliotecausj.odilotk.es", "https://crai-usta.odilotk.es",
                "https://bibliotecadigital.usm.cl", "https://universidadunie.odilo.es", "https://universidadviu.odilotk.es",
                "https://ual.odilotk.es", "https://bibliotecauaysen.odilotk.es", "https://utadeo.unlimitedlearning.io",
                "https://bibliotecaucundinamarca.odilotk.es", "https://bibliotecaugr.odilotk.es", "https://unimagdalena.unlimitedlearning.io",
                "https://udenar.unlimitedlearning.io", "https://buo.odilotk.es", "https://cielo.usal.es",
                "https://us.odilotk.es", "https://bibliotecautalca.odilotk.es", "https://utolima.unlimitedlearning.io",
                "https://bibliotecadigitalodilo.uv.cl", "https://uniandes.odilotk.es", "https://produccionbibliografica.unisinu.edu.co",
                "https://uic.odilotk.es", "https://uv.odilotk.es", "https://knowledgeumak.odilo.us",
                "https://unlimitedleadership.odilo.us", "https://vakantieb.odilotk.es", "https://library.espired.com",
                "https://bega-elbe2.edu.xunta.es", "https://yvl.odilo.us", "https://zinkers.fundacionrepsol.com",
                "https://pre-onlinebibliotheek.odilotk.es", "https://onlinebibliotheek.odilotk.es", "https://madrid.odilotk.es",
                "https://ebook.pozuelodealarcon.org"
        );

        List<String> isbnFoundsUrls = new ArrayList<>();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

        for (String baseUrl : urls) {
            String fullUrl = baseUrl + searchParams;

            if (UrlUtils.isUrlReachable(fullUrl) && !UrlUtils.checkForRedirection(fullUrl)) {

                // Tot el procés de navegació va DINS del try-catch
                try {
                    driver.get(fullUrl);
                    System.out.println("Navigating to: " + fullUrl);

                    // Millorem la detecció de logins: no només Microsoft, sinó qualsevol URL que contingui "login" o "auth"
                    String currentUrl = driver.getCurrentUrl();
                    if (currentUrl.contains("login") || currentUrl.contains("auth")) {
                        System.out.println("Redirected to a login page, skipping: " + currentUrl);
                        continue; // Saltem a la següent iteració del bucle
                    }

                    // Wait for the results element to be visible
                    // Si la pàgina no ha carregat correctament (ex. una altra redirecció rara), això llançarà TimeoutException i anirà al catch
                    WebElement results = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/app-root/main/app-results-component/div[2]/div[2]/app-format-filter/div/div/div")));

                    // Handle cookies dialog if present
                    By cookiesDialog = By.xpath("/html/body/div[1]/div[3]/div/mat-dialog-container/app-cookies-dialog");
                    if (NavigationActions.elementExists(driver, cookiesDialog)) {
                        System.out.println("Accepting cookies...");
                        NavigationActions.hoverAndClick(driver, By.xpath("/html/body/div[1]/div[3]/div/mat-dialog-container/app-cookies-dialog/div/div[2]/div/button[2]"));
                    }

                    if (results.isDisplayed()) {
                        System.out.println("ISBN found in " + baseUrl);
                        // Click on the first result
                        NavigationActions.hoverAndClick(driver, By.xpath("//app-results-item/app-card-item/article/section/opac-record-cover/div/img"));
                        // Pause for 2 seconds
                        Thread.sleep(2000);
                        System.out.println("Adding Book URL: " + driver.getCurrentUrl());
                        isbnFoundsUrls.add(driver.getCurrentUrl());
                    } else {
                        System.out.println("No results found.");
                    }

                } catch (TimeoutException e) {
                    // Si no troba l'element de resultats en 2 segons, ho capturem aquí i el bucle segueix viu
                    System.out.println("Results element not found (Timeout) for URL: " + fullUrl);
                } catch (Exception e) {
                    // Per si hi ha qualsevol altre error inesperat (ex. el navegador es penja un moment)
                    System.out.println("Unexpected error processing " + fullUrl + ": " + e.getMessage());
                }

            } else {
                System.out.println("URL not reachable or redirected initially: " + fullUrl);
            }
        }

        // Dump the list to the file
        DumpListToFile.dumpListToFile(isbnFoundsUrls, pathForDownloads + "searches_" + isbn + ".txt");
        try {
            List<String> lines = Files.readAllLines(Paths.get(pathForDownloads + "searches_" + isbn + ".txt"));
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}