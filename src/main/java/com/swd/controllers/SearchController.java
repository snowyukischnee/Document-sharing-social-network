package com.swd.controllers;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.swd.db.documents.models.MongoDaoBaseClass;
import com.swd.db.relationships.models.PostRepository;
import com.swd.viewmodels.PostViewModel;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
public class SearchController {
    @Autowired
    PostRepository postRepository;

    @RequestMapping(value = "/search/documents", method = RequestMethod.GET)
    @ResponseBody
    public String search(
            @RequestParam(name = "lbound", required = false) Integer lower_bound,
            @RequestParam(name = "no_items", required = false) Integer no_items,
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "sort", required = false) String sort) {
        Gson gson = new Gson();
        MongoDaoBaseClass<com.swd.db.documents.entities.Post> postdao = null;
        List<PostViewModel> result = new ArrayList<>();
        if (sort == null) sort = "date_created";
        if (sort.equalsIgnoreCase("date_created")) {
            postdao = new MongoDaoBaseClass<com.swd.db.documents.entities.Post>("post") {
                @Override
                public List<Document> List_custom() {
                    List<Document> arr = null;
                    FindIterable<Document> docs = null;
                    if (query != null) {
                        Pattern regex = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
                        docs = collection.find(
                                Filters.and(
                                        Filters.eq("enabled", true),
                                        Filters.or(
                                                Filters.eq("title", regex),
                                                Filters.eq("description", regex)
                                        )
                                )
                        ).sort(Sorts.descending("dateCreated"));
                    } else {
                        docs = collection.find(
                                Filters.eq("enabled", true)
                        ).sort(Sorts.descending("dateCreated"));
                    }
                    if (lower_bound != null & no_items != null) {
                        docs = docs.skip(lower_bound).limit(no_items);
                    }
                    arr = docs.into(new ArrayList<>());
                    return arr;
                }
            };
            List<Document> arr = postdao.List_custom();
            try {
                for (Document doc : arr) result.add(new PostViewModel(doc.getObjectId("_id")));
            } catch (Exception e) {
                e.printStackTrace();
                Map<String, String> result_e = new HashMap<>();
                result_e.put("Status", "ERROR");
                result_e.put("Message", "Failed to search by date_created");
                return gson.toJson(result_e);
            }
        } else if (sort.equalsIgnoreCase("publication_date")) {
            postdao = new MongoDaoBaseClass<com.swd.db.documents.entities.Post>("post") {
                @Override
                public List<Document> List_custom() {
                    List<Document> arr = null;
                    FindIterable<Document> docs = null;
                    if (query != null) {
                        Pattern regex = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
                        docs = collection.find(
                                Filters.and(
                                        Filters.eq("enabled", true),
                                        Filters.or(
                                                Filters.eq("title", regex),
                                                Filters.eq("description", regex)
                                        )
                                )
                        ).sort(Sorts.descending("publicationDate"));
                    } else {
                        docs = collection.find(
                                Filters.eq("enabled", true)
                        ).sort(Sorts.descending("publicationDate"));
                    }
                    if (lower_bound != null & no_items != null) {
                        docs = docs.skip(lower_bound).limit(no_items);
                    }
                    arr = docs.into(new ArrayList<>());
                    return arr;
                }
            };
            List<Document> arr = postdao.List_custom();
            try {
                for (Document doc : arr) result.add(new PostViewModel(doc.getObjectId("_id")));
            } catch (Exception e) {
                e.printStackTrace();
                Map<String, String> result_e = new HashMap<>();
                result_e.put("Status", "ERROR");
                result_e.put("Message", "Failed to search by publication_date");
                return gson.toJson(result_e);
            }
        } else if (sort.equalsIgnoreCase("most_followed")) {
            List<com.swd.db.relationships.entities.Post> arr_rel = null;
            if (lower_bound != null & no_items != null) {
                arr_rel = postRepository.getMostFollowedPostsWBound(lower_bound, no_items);
            } else {
                arr_rel = postRepository.getMostFollowedPosts();
            }
            System.out.println(arr_rel);
            postdao = new MongoDaoBaseClass<com.swd.db.documents.entities.Post>("post") {
                @Override
                public Document Search_custom(ObjectId _id) {
                    Document doc = null;
                    if (query != null) {
                        Pattern regex = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
                        doc = collection.find(
                                Filters.and(
                                        Filters.eq("_id", _id),
                                        Filters.eq("enabled", true),
                                        Filters.or(
                                                Filters.eq("title", regex),
                                                Filters.eq("description", regex)
                                        )
                                )
                        ).first();
                    } else {
                        doc = collection.find(
                                Filters.and(
                                        Filters.eq("_id", _id),
                                        Filters.eq("enabled", true)
                                )
                        ).first();
                    }
                    return doc;
                }
            };
            try {
                for (com.swd.db.relationships.entities.Post post_rel : arr_rel) {
                    Document doc = postdao.Search_custom(new ObjectId(post_rel.getHex_string_id()));
                    if (doc != null) result.add(new PostViewModel(new ObjectId(post_rel.getHex_string_id())));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Map<String, String> result_e = new HashMap<>();
                result_e.put("Status", "ERROR");
                result_e.put("Message", "Failed to search by most_followed");
                return gson.toJson(result_e);
            }
        } else {
            Map<String, String> result_e = new HashMap<>();
            result_e.put("Status", "ERROR");
            result_e.put("Message", "Operation not supported yet");
            return gson.toJson(result_e);
        }
        return gson.toJson(result);
    }
}
