/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sfedu.maven1.dataProviders;

import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
/**
 * Класс-контейнер для XML
 * @author sp2
 */
@Root(name = "main")
public class WrapperXML<T> {
    @ElementList(inline = true, required = false)
    public List<T> list;

    public WrapperXML() {
    }

    public WrapperXML(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
