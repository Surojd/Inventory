package com.devluk.inventory.controller;

import com.devluk.inventory.dao.GenericDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.BufferedReader;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "application/json")
@RequestMapping("/json")
public class RESTController {

    @Autowired
    private GenericDao genericDao;

    @RequestMapping(value = "/{table}",
            consumes = "application/json",
            method = RequestMethod.POST)
    public <T> int[] insertData(
            @PathVariable("table") String table,
            HttpServletRequest request) {
        try {
            Class cl = genericDao.ClassOf(table);
            BufferedReader br = request.getReader();
            String jsonData = "";
            String read;
            while ((read = br.readLine()) != null) {
                jsonData += read;
            }
            JSONParser map = new JSONParser();
            ObjectMapper mapper = new ObjectMapper();
            if (jsonData.trim().startsWith("{")) {
                Object obj = mapper.convertValue(map.parse(jsonData), cl);
                int i = genericDao.saveOrUpdate(cl, obj);
                return new int[]{i};
            } else if (jsonData.trim().startsWith("[")) {
                List list = mapper.readValue(jsonData, TypeFactory.defaultInstance().constructCollectionType(List.class, cl));
                int a[] = new int[list.size()];
                int sn = 0;
                for (Object obj : list) {
                    int i = genericDao.saveOrUpdate(cl, obj);
                    a[sn++] = i;
                }
                return a;
            }
            return new int[]{0};
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
            return new int[]{0};
        }
    }

//    @RequestMapping(value = "/{table}",
//            produces = "application/json",
//            method = RequestMethod.GET)
//    public <T> T getData(@PathVariable("table") String table) {
//        System.out.println(table);
//        try {
//            //        return (T) genericDao.getByID(Users.class, 1);
//            return (T) genericDao.getAllData(table);
//        } catch (Exception ex) {
//            return (T) "error";
//        }
//    }
    @RequestMapping(value = "/{table}",
            produces = "application/json",
            method = RequestMethod.GET)
    public <T> T getData(@PathVariable("table") String table, HttpServletRequest request) {
        System.out.println(table);
        Map<String, String[]> l = request.getParameterMap();
        try {
            if (l.size() <= 0) {
                return (T) genericDao.getAllData(table);
            } else {
                return (T) genericDao.getAllData(table, l);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return (T) "error";
        }
    }

    @RequestMapping(value = "/{table}/{id}", produces = "application/json", method = RequestMethod.GET)
    public <T> T getDataByID(@PathVariable("table") String table,
            @PathVariable("id") int id) {
        System.out.println(table);
        try {
            return (T) genericDao.getByID(table, id);
        } catch (Exception ex) {
            ex.printStackTrace();
            return (T) "error";
        }
    }
}
