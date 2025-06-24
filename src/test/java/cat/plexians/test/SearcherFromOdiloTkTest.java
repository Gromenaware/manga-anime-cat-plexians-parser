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

        String isbn = "9788416280315";

        String pathForDownloads = File.separator + "Volumes" + File.separator + "TeraTwo" + File.separator + "odilotk_searches" + File.separator;

        String searchParams = File.separator + "results?limit=24&offset=0&query=allfields_txt:" + isbn + "&order=relevance:desc";

        //Preparem els directoris
        parsingUtils.directoryCreation(pathForDownloads);

        //List 20250212 Complete
        List<String> urls = Arrays.asList("https://colegiobrasil.odilo.us", "https://colegiolatam.odilo.es", "https://academic.odilo.us", "https://biblioteca.odilo.us", "https://business-school.odilo.es", "https://business-school.odilo.us", "https://corporate.odilo.es", "https://corporativobrasil.odilo.us", "https://library.odilo.us", "https://colegio.odilo.es", "https://university.odilo.es", "https://university.odilo.us", "https://workplace.odilo.us", "https://demo-academics.odilo.us", "https://acciona.unlimitedlearning.io", "https://adriandistrictlibrary.odilo.us", "https://bibliotecadigitalubaque.odilotk.es", "https://biblioteca.asambleamurcia.es", "https://asambleaex.odilotk.es", "https://ascendger.odilo.us", "https://aim.odilo.us", "https://www.theologicalebooks.org", "https://aytotinajo.odilotk.es", "https://bibliotecasdaenergia.odilo.us", "https://www.bpdigital.cl", "https://bi-blioteca.odilotk.es", "https://masconectadosbch.odilotk.es", "https://baskent.unlimitedlearning.io", "https://beatrice.odilo.us", "https://bibliotecacortsvalencianes.odilotk.es", "https://bibliotecadigitalercilianarvaez.odilo.es", "https://mehextranet.odilotk.es", "https://ga-p.odilo.es", "https://bibliotecaparquedigital.odilo.us", "https://descubrelo.riecken.org", "https://catalogobibliotecas.cartagena.es", "https://bigelowlibrary.odilo.us", "https://bcls.odilo.us", "https://brentwoodnylibrary.odilo.us", "https://brocktonpubliclibrary.odilo.us", "https://school.odilo.es", "https://sanagustin.unlimitedlearning.io", "https://agoravirtual.bibliotecaceu.es", "https://cfcst.odilo.us", "https://cftestatales.odilotk.es", "https://bibliotecadigital.coit.es", "https://copib.odilotk.es", "https://calasancio.unlimitedlearning.io", "https://cca.unlimitedlearning.io", "https://cesur.unlimitedlearning.io", "https://cmu.odilo.us", "https://cloudlearning.odilo.us", "https://jjcastromartinez.unlimitedlearning.io", "https://newland.unlimitedlearning.io", "https://bibliotecacopc.odilotk.es", "https://copmadrid.odilo.es", "https://cometa.unlimitedlearning.io", "https://bibliotecadigital.comfaboy.com.co", "https://bibliotecadigital.comfachoco.com.co", "https://bibliotecadigital.comfaoriente.com", "https://bibliotecaconferenciaepiscopal.odilo.es", "https://retoleermas.odilo.us", "https://biblioccyl.odilo.es", "https://crecimientopersonal.odilo.es", "https://conocimientoaunclic.ccb.org.co", "https://demo-dglab.unlimitedlearning.io", "https://demo.odilo.es", "https://demo-moe.odilo.us", "https://demo.odilo.us", "https://collections.odilo.us", "https://eserp.odilotk.es", "https://eude.odilotk.es", "https://www.ebiblioandorra.ad", "https://econet.unlimitedlearning.io", "https://bam.iespe.edu.mx", "https://suscripciones.paidotribopremium.com", "https://elebiblioteca.odilotk.es", "https://executive.odilo.us", "https://melies.odilo.us", "https://firstmutual.unlimitedlearning.io", "https://flo.odilo.us", "https://formacionydesarrollo.odilo.es", "https://fvrl.odilo.us", "https://sdespierto.odilotk.es", "https://glac.odilo.us", "https://hditeca.unlimitedlearning.io", "https://goldenrey.odilo.us", "https://googlesg.unlimitedlearning.io", "https://gecoas.odilotk.es", "https://redaprende.unlimitedlearning.io", "https://grupopichincha.unlimitedlearning.io", "https://school.odilo.es", "https://inap.odilo.es", "https://iteso.odilotk.es", "https://innovaschools.odilotk.es", "https://centrodocaya.odilotk.es", "https://ipss.odilo.us", "https://interbooks-corporate.odilo.es", "https://interbooks-university.odilo.es", "https://johnjermain.odilo.us", "https://online-library.jmc.edu.ph", "https://kenailibrary.odilo.us", "https://laensenanzabaq.unlimitedlearning.io", "https://lebanonlibrary.odilo.us", "https://lintac.odilotk.es", "https://mitbiology.odilotk.es", "https://mscmarinduque.odilo.us", "https://memoriadigital-formacion.odilo.es", "https://metrolibrary.odilo.us", "https://bibliotecamineducec.odilotk.es", "https://ministerioeducacion.odilo.us", "https://bdpenalolen.odilo.us", "https://nashville.odilo.us", "https://library.bnr.rw", "https://nlp.odilo.us", "https://newbraunfelslibrary.odilo.us", "https://formacionprofesional.odilo.es", "https://keycompetences.unlimitedlearning.io", "https://textbookevolution.unlimitedlearning.io", "https://testotk-auto.odilotk.es", "https://www.ebiblioandorra.ad", "https://odiloib.unlimitedlearning.io", "https://languagelearning.unlimitedlearning.io", "https://lifelong.odilo.es", "https://literacy.unlimitedlearning.io", "https://odilofy.odilo.es", "https://odilolab.unlimitedlearning.io", "https://osceolalibrary.odilo.us", "https://prima.odilo.us", "https://parlamentcatalunya.odilo.cat", "https://pflugerville.odilo.us", "https://plantation.odilo.us", "https://laschapas.unlimitedlearning.io", "https://testotk.odilotk.es", "https://poudreriver.odilo.us", "https://pmilearning.unlimitedlearning.io", "https://reading.odilo.us", "https://nymasoniclibrary.odilo.us", "https://roundrock.odilo.us", "https://seameo-innotech.odilo.us", "https://bibliotecasebraemg.unlimitedlearning.io", "https://bibliosek.odilotk.es", "https://shs-adc.unlimitedlearning.io", "https://skoda.odilotk.es", "https://steam.unlimitedlearning.io", "https://bibliotecasgc.odilotk.es", "https://ssumainlib.odilo.us", "https://sanbeda-alabang.odilo.us", "https://sanbenildorizal.unlimitedlearning.io", "https://smcl.odilo.us", "https://sccl.odilo.us", "https://southernchristiancollege.odilo.es", "https://stjohnlib.odilo.us", "https://sustainabilityd-i.odilo.es", "https://teachertraining.unlimitedlearning.io", "https://thecore.odilo.es", "https://dibridigital.ucsh.cl", "https://bibliotecauniacc.odilotk.es", "https://catalogobiblioteca.unir.net", "https://vlib.unired.edu.co", "https://bibunimonserrate.odilotk.es", "https://library.ucatolica.edu.co", "https://bibliotecadigital.ufv.es", "https://bibliotecaupn.odilo.us", "https://bibliotecausj.odilotk.es", "https://universidadunie.odilo.es", "https://universidadviu.odilotk.es", "https://ual.odilotk.es", "https://bibliotecauaysen.odilotk.es", "https://utadeo.unlimitedlearning.io", "https://bibliotecaugr.odilotk.es", "https://buo.odilotk.es", "https://us.odilotk.es", "https://bibliotecautalca.odilotk.es", "https://utolima.unlimitedlearning.io", "https://bibliotecadigitalodilo.uv.cl", "https://produccionbibliografica.unisinu.edu.co", "https://uic.odilotk.es", "https://knowledgeumak.odilo.us", "https://unlimitedleadership.odilo.us", "https://volusialibrary.odilo.us", "https://yvl.odilo.us");

        List<String> isbnFoundsUrls = new ArrayList<>();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));


        for (String baseUrl : urls) {
            String fullUrl = baseUrl + searchParams;

            if (UrlUtils.isUrlReachable(fullUrl) && !UrlUtils.checkForRedirection(fullUrl)) {
                driver.get(fullUrl);
                System.out.println("Navigating to: " + fullUrl);

                wait.until(ExpectedConditions.urlToBe(fullUrl));


                try {
                    // Wait for the results element to be visible
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
                    System.out.println("Results element not found for URL: " + fullUrl);
                }
            } else {
                System.out.println("URL not reachable: " + fullUrl);
            }
        }
        // Dump the list to the file
        DumpListToFile.dumpListToFile(isbnFoundsUrls, pathForDownloads + "searches_" + isbn + ".txt");
        try {
            // Replace "file.txt" with your file path
            List<String> lines = Files.readAllLines(Paths.get(pathForDownloads + "searches_" + isbn + ".txt"));
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}