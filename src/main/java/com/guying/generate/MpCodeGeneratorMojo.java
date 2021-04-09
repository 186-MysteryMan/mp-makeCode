package com.guying.generate;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author guying
 */
@Mojo(name = "generator",defaultPhase = LifecyclePhase.COMPILE)
public class MpCodeGeneratorMojo extends AbstractMojo {

    private static final String DEFAULT_PATH = "mpGenerate.yaml";

    private static final Logger logger = LoggerFactory.getLogger(AutoGenerator.class);

    @Parameter(property = "configuration.file")
    private String configurationFile;

    public void execute() {
        logger.info("------------------开始执行-----------------------");
        InputStream inputStream = null;
        if (!StringUtils.isEmpty(configurationFile)) {
            try {
                inputStream = new FileInputStream(configurationFile);
            } catch (FileNotFoundException e) {
                logger.error("读取配置文件异常,异常信息:{}",e.getMessage(),e);
                e.printStackTrace();
            }
        } else {
            inputStream = MpCodeGeneratorMojo.class.getClassLoader().getResourceAsStream(DEFAULT_PATH);
        }
        logger.info("-----------------开始读取配置文件-----------------------");
        try{
            Yaml yaml = new Yaml();
            yaml.setBeanAccess(BeanAccess.FIELD);
            AutoGenerator generator = yaml.loadAs(inputStream, AutoGenerator.class);
            StrategyConfig strategy = generator.getStrategy();
            List<TableFill> tableFillList = new ArrayList<>();
            tableFillList.add(new TableFill("creator_id", FieldFill.INSERT));
            tableFillList.add(new TableFill("creator_name", FieldFill.INSERT));
            strategy.setTableFillList(tableFillList);
            generator.execute();
        }catch (Exception e){
            logger.error("读取配置文件异常,异常信息:{}",e.getMessage(),e);
        }
    }
}
