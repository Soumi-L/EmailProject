package com.example.ThymleafExemple.Configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ApplicationConfiguration {

    //Les Templates Resolvers ==> Des objets sont chargés de déterminer comment nos modèles seront accessibles
    @Bean
    @Description("Thymeleaf template resolver serving HTML 5")
    public ClassLoaderTemplateResolver templateResolver() {

        var templateResolver = new ClassLoaderTemplateResolver();

        //templateResolver.setPrefix("/templates/");
        templateResolver.setCacheable(false);
        //templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding("UTF-8");
        System.out.println("=============================================templateResolver============================================================");
        return templateResolver;
    }

    @Bean
    @Description("Thymeleaf template engine with Spring integration")
    public SpringTemplateEngine templateEngine() {

        var templateEngine = new SpringTemplateEngine();

        templateEngine.setTemplateResolver(templateResolver());
        System.out.println("=============================================templateEngine==============================================================");
        return templateEngine;
    }
    //end
    //Notre moteur de modèles est maintenant prêt
    // et nous pouvons commencer à créer nos pages en utilisant Thymeleaf.
}
