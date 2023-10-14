package ua.kiev.prog;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;


import java.io.*;
import java.util.*;

@Controller
public class MyController {
    static final int DEFAULT_GROUP_ID = -1;
    static final int ITEMS_PER_PAGE = 6;

    private final ContactService contactService;

    private String patternMemo;
    private Model currentModel;

    public MyController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/")
    public String index(Model model, @RequestParam(required = false, defaultValue = "0") Integer page) {
        if (page < 0) {
            page = 0;
        }

        List<Contact> contacts = contactService.findAll(PageRequest.of(page, ITEMS_PER_PAGE, Sort.Direction.DESC, "id"));

        model.addAttribute("groups", contactService.findGroups());
        model.addAttribute("contacts", contacts);
        model.addAttribute("allPages", getPageCount());
        model.addAttribute("mode", "AllContacts");

        currentModel = model;
        return "index";
    }

    @GetMapping("/reset")
    public String reset() {
        contactService.reset();
        return "redirect:/";
    }

    @GetMapping("/saveToCSV")
    public String saveToCSV() throws IOException {
        try (FileWriter fw = new FileWriter("results.csv"); CSVPrinter csvPrinter = new CSVPrinter(fw, CSVFormat.DEFAULT)) {
            if (currentModel.getAttribute("mode").equals("byID")) {
                csvPrinter.printRecords(contactService.findByGroup((Group) currentModel.getAttribute("group")));
            } else if (currentModel.getAttribute("mode").equals("search")) {
                csvPrinter.printRecords(contactService.findByPattern(patternMemo));
            } else {
                csvPrinter.printRecords(contactService.findAll());
            }
            return "save_to_csv_page";
        }
    }

    @GetMapping("/loadFromCSVpage")
    public String loadFromCSVpage() throws IOException {
        return "loadFromCSVpage";
    }

    @PostMapping("/loadCSV")
    public String loadFromCSV(@RequestParam(required = false) String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while (line != null) {
                line = line.substring(line.indexOf('{') + 1, line.indexOf('}'));
                String[] arrTemp = line.split(",");
                Map<String, String> mapTemp = new HashMap<String, String>();
                for (String arrElem : arrTemp) {
                    String[] subElement = arrElem.trim().split(":");
                    mapTemp.put(subElement[0], subElement[1]);
                }
                System.out.println(mapTemp);
                String name = mapTemp.get("name");
                String surname = mapTemp.get("surname");
                String phone = mapTemp.get("phone");
                String email = mapTemp.get("email");

                Contact contactTemp = new Contact(contactService.findGroupByName(mapTemp.get("group")), name, surname, phone, email);
                contactService.addContact(contactTemp);

                line = br.readLine();
            }
        }
        return "redirect:/";
    }

    @GetMapping("/contact_add_page")
    public String contactAddPage(Model model) {
        model.addAttribute("groups", contactService.findGroups());
        currentModel = model;
        return "contact_add_page";
    }

    @GetMapping("/group_add_page")
    public String groupAddPage() {
        return "group_add_page";
    }

    @GetMapping("/group/{id}")
    public String listGroup(@PathVariable(value = "id") long groupId, @RequestParam(required = false, defaultValue = "0") Integer page, Model model) {
        Group group = (groupId != DEFAULT_GROUP_ID) ? contactService.findGroup(groupId) : null;
        if (page < 0) {
            page = 0;
        }

        List<Contact> contacts = contactService.findByGroup(group, PageRequest.of(page, ITEMS_PER_PAGE, Sort.Direction.DESC, "id"));

        model.addAttribute("groups", contactService.findGroups());
        model.addAttribute("contacts", contacts);
        model.addAttribute("byGroupPages", getPageCount(group));
        model.addAttribute("groupId", groupId);
        model.addAttribute("mode", "byID");
        model.addAttribute("group", group);

        currentModel = model;

        return "index";
    }

    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public String search(@RequestParam(required = false, value = "pattern") String pattern, @RequestParam(value = "page", required = false) Integer page, Model model) {

        if (page == null || page < 0) {
            page = 0;
        }

        patternMemo = (pattern != null && !Objects.equals(patternMemo, pattern)) ? pattern : patternMemo;

        patternMemo = (patternMemo == null) ? pattern : patternMemo;

        List<Contact> contacts = contactService.findByPattern(patternMemo, PageRequest.of(page, ITEMS_PER_PAGE, Sort.Direction.DESC, "id"));

        model.addAttribute("contacts", contacts);
        model.addAttribute("patternPages", getPageCount(patternMemo));
        model.addAttribute("pattern", patternMemo);
        model.addAttribute("currentPage", page);
        model.addAttribute("mode", "search");

        currentModel = model;

        return "index";
    }

    @PostMapping(value = "/contact/delete")
    public ResponseEntity<Void> delete(@RequestParam(value = "toDelete[]", required = false) long[] toDelete) {
        if (toDelete != null && toDelete.length > 0) {
            contactService.deleteContacts(toDelete);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/contact/add")
    public String contactAdd(@RequestParam(value = "group") long groupId, @RequestParam String name, @RequestParam String surname, @RequestParam String phone, @RequestParam String email) {
        Group group = (groupId != DEFAULT_GROUP_ID) ? contactService.findGroup(groupId) : null;

        Contact contact = new Contact(group, name, surname, phone, email);
        contactService.addContact(contact);

        return "redirect:/";
    }

    @PostMapping(value = "/group/add")
    public String groupAdd(@RequestParam String name) {
        contactService.addGroup(new Group(name));
        return "redirect:/";
    }

    private long getPageCount() {
        long totalCount = contactService.count();
        return (totalCount / ITEMS_PER_PAGE) + ((totalCount % ITEMS_PER_PAGE > 0) ? 1 : 0);
    }

    private long getPageCount(Group group) {
        long totalCount = contactService.countByGroup(group);
        return (totalCount / ITEMS_PER_PAGE) + ((totalCount % ITEMS_PER_PAGE > 0) ? 1 : 0);
    }

    private long getPageCount(String pattern) {
        long totalCount = contactService.countByPattern(pattern);
        return (totalCount / ITEMS_PER_PAGE) + ((totalCount % ITEMS_PER_PAGE > 0) ? 1 : 0);
    }
}
