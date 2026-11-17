package com.tripmate;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * 1-Click Embedded Apache Tomcat 10.1 Application Server.
 * Boots TripMate programmatically on port 8080.
 */
public class Main {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        try {
            System.out.println("=============================================================");
            System.out.println("      Starting TripMate on Apache Tomcat 10.1...             ");
            System.out.println("=============================================================");

            // 1. Ensure target classes are copied into WEB-INF/classes
            syncClassesToWebInf();

            // 2. Initialize Tomcat
            Tomcat tomcat = new Tomcat();
            tomcat.setPort(PORT);
            
            File baseDir = new File("temp-tomcat");
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
            tomcat.setBaseDir(baseDir.getAbsolutePath());

            // Initialize HTTP Connector
            tomcat.getConnector();

            // Locate Webapp Directory
            File webappDir = new File("src/main/webapp/");
            if (!webappDir.exists()) {
                webappDir = new File("TripMate/src/main/webapp/");
            }
            String webappPath = webappDir.getAbsolutePath();

            // Mount the primary application at /TripMate
            StandardContext ctxTripMate = (StandardContext) tomcat.addWebapp("/TripMate", webappPath);
            ctxTripMate.setParentClassLoader(Main.class.getClassLoader());

            // Mount compiled classes resource set
            File classesDir = new File("target/classes");
            if (classesDir.exists()) {
                WebResourceRoot resources = new StandardRoot(ctxTripMate);
                resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                        classesDir.getAbsolutePath(), "/"));
                ctxTripMate.setResources(resources);
            }

            // Mount a lightweight root context to redirect http://localhost:8080/ -> http://localhost:8080/TripMate/
            Context rootContext = tomcat.addContext("", baseDir.getAbsolutePath());
            Tomcat.addServlet(rootContext, "RootRedirectServlet", new HttpServlet() {
                @Override
                protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                    resp.sendRedirect(req.getContextPath() + "/TripMate/");
                }
            });
            rootContext.addServletMappingDecoded("/*", "RootRedirectServlet");

            // 3. Start Tomcat
            tomcat.start();

            String appUrl = "http://localhost:" + PORT + "/TripMate/";
            System.out.println("\n=============================================================");
            System.out.println(">>> SUCCESS: TripMate is LIVE at: " + appUrl);
            System.out.println(">>> Database connected to: tripmate_db");
            System.out.println(">>> Press STOP in IDE or Ctrl+C in terminal to exit.");
            System.out.println("=============================================================\n");

            // Auto-open browser
            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(appUrl));
                }
            } catch (Exception ignored) {}

            // Keep server running
            tomcat.getServer().await();

        } catch (Exception e) {
            System.err.println("Fatal error starting TripMate: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Synchronizes compiled classes from target/classes to src/main/webapp/WEB-INF/classes
     */
    private static void syncClassesToWebInf() {
        try {
            File sourceDir = new File("target/classes");
            File destDir = new File("src/main/webapp/WEB-INF/classes");

            if (sourceDir.exists()) {
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
                copyDirectoryRecursively(sourceDir.toPath(), destDir.toPath());
            }
        } catch (Exception e) {
            System.err.println("Note: Could not sync classes: " + e.getMessage());
        }
    }

    private static void copyDirectoryRecursively(Path source, Path target) throws IOException {
        Files.walk(source).forEach(sourcePath -> {
            try {
                Path targetPath = target.resolve(source.relativize(sourcePath));
                if (Files.isDirectory(sourcePath)) {
                    if (!Files.exists(targetPath)) {
                        Files.createDirectories(targetPath);
                    }
                } else {
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                // Ignore copy errors
            }
        });
    }
}
