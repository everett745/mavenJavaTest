/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sfedu.maven1.dataProviders;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import ru.sfedu.maven1.Constants;
import ru.sfedu.maven1.model.User;
import ru.sfedu.maven1.utils.PropertyProvider;

/**
 *
 * @author sp2
 */
public class DataProviderXML {
    private static Logger log = LogManager.getLogger(DataProviderXML.class);
    
    public void insertUser(List<User> bean1List) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, Exception{
        try{
            //Проверяем, создан ли файл? Если нет, то создаём
            (new File(this.getFilePath(User.class))).createNewFile();
            //Подключаемся к потоку записи файла
            FileWriter writer = new FileWriter(this.getFilePath(User.class), false);
            //Определяем сериалайзер
            Serializer serializer = new Persister();
            
            //Определяем контейнер, в котором будут находиться все объекты
            WrapperXML<User> xml = new WrapperXML<User>();
            //Записываем список объектов в котнейнер
            xml.setList(bean1List);
            
            //Записываем в файл
            serializer.write(xml, writer);
        }catch(IndexOutOfBoundsException e){
            log.error(e);
        }
    }
    
    public User getUserById(UUID id) throws IOException, Exception{
        List<User> list = this.select(User.class);
        try{
            User bean1=list.stream()
                    .filter(el->el.getId()==id)
                    .limit(1)
                    .findFirst().get();
            return bean1;
        }catch(NoSuchElementException e){
            log.error(e);
            return null;
        }
    }
    
    public <T> List<T> select(Class cl) throws IOException, Exception{
        //Подключаемся к считывающему потоку из файла
        FileReader fileReader = new FileReader(this.getFilePath(cl));
        //Определяем сериалайзер
        Serializer serializer = new Persister();
        //Определяем контейнер и записываем в него объекты
        WrapperXML xml = serializer.read(WrapperXML.class, fileReader);
        //Если список null, то делаем его пустым списком
        if (xml.getList() == null) xml.setList(new ArrayList<T>());
        //Возвращаем список объектов
        return xml.getList();
    }
    
    /**
     * Получаем путь к файлу
     * @param cl
     * @return
     * @throws IOException 
     */
    private String getFilePath(Class cl) throws IOException{
        return PropertyProvider.getProperty(Constants.XML_PATH)
                + cl.getSimpleName().toString().toLowerCase()
                + PropertyProvider.getProperty(Constants.XML_EXTENSION);
    }
}
