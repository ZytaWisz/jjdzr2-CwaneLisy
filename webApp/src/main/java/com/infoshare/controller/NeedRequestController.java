package com.infoshare.controller;

import com.infoshare.domain.TypeOfHelp;
import com.infoshare.formObjects.NeedRequestForm;
import com.infoshare.service.NeedRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("need-request")
public class NeedRequestController {
    private final NeedRequestService needRequestService;

    @Autowired
    public NeedRequestController(NeedRequestService needRequestService) {
        this.needRequestService = needRequestService;
    }

  @PostMapping("/need-request-form")
  public String createNeedRequestFromForm(@ModelAttribute("needRequestForm") NeedRequestForm needRequestForm) {
    System.out.println(needRequestForm.getName());
    System.out.println(needRequestForm.getLocation());
    System.out.println(needRequestForm.getPhone());
    System.out.println(needRequestForm.getTypeOfHelp());
    needRequestService.createNeedRequest(needRequestForm.getName(), needRequestForm.getLocation(), needRequestForm.getPhone(), needRequestForm.getTypeOfHelp());
    return "start";
  }

    @GetMapping("/create")
    public String createNeedRequest(Model model) {
      model.addAttribute(new NeedRequestForm());
      List<TypeOfHelp> typeOfHelp = Arrays.asList(TypeOfHelp.values());

      model.addAttribute("types", typeOfHelp);
      return "createNeedRequestForm";
    }

    @GetMapping("/search")
    public String searchForNeedRequest(Model model) {
        return getTestViewWithPageUnderConstructionMessage(model);
    }

    @GetMapping("/edit")
    public String editNeedRequest(Model model) {
        return getTestViewWithPageUnderConstructionMessage(model);
    }

    @GetMapping("/associate-to-volunteer")
    public String associateNeedRequestToVolunteer(Model model) {
        return getTestViewWithPageUnderConstructionMessage(model);
    }

    @GetMapping("/add-comment")
    public String addCommentToNeedRequest(Model model) {
        return getTestViewWithPageUnderConstructionMessage(model);
    }

    @GetMapping("/browse-history")
    public String browseHistoryOfNeedRequest(Model model) {
        return getTestViewWithPageUnderConstructionMessage(model);
    }

    private String getTestViewWithPageUnderConstructionMessage(Model model) {
        model.addAttribute("message", "This page is under construction...");
        return "test-view";
    }
}
