package com.infoshare.controller;

import com.infoshare.dto.NeedRequestFilterForm;
import com.infoshare.filteringservice.NeedRequestFilteringService;
import com.infoshare.formobjects.NeedRequestForm;
import com.infoshare.service.NeedRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;
import java.util.function.BiConsumer;

@Controller
@RequestMapping("need-request")
public class NeedRequestController {
    public static final String EDIT_FORM_ACTION_URL = "actionUrl";
    public static final String NEW_FORM_ACTION_URL = "newActionUrl";

    public static final String EDIT_NEED_REQUEST_URL = "edit-need-request";
    public static final String SUBMIT_NEW_NEED_REQUEST_URL = "submit-new-form";
    public static final String NEED_REQUEST_LIST_VIEW = "need-request-list";
    public static final String REDIRECT_NEED_REQUEST_ALL = "redirect:/need-request/all";

    public static final String FILTER_FORM_ATTR = "filterForm";
    public static final String NEW_NEED_REQUEST_ATTR = "newNeedRequest";
    public static final String EDIT_NEED_REQUEST_ATTR = "editNeedRequest";
    public static final String NEED_REQUESTS_LIST_ATTR = "needRequestsList";

    public static final String STATUSES_OF_HELP_ATTR = "statusesOfHelp";
    public static final String TYPES_ATTR = "types";

    public static final String HAS_ERRORS_ATTR = "hasErrors";
    public static final String NEW_HAS_ERRORS_ATTR = "newHasErrors";

    private final NeedRequestService needRequestService;
    private final NeedRequestFilteringService needRequestFilteringService;

    BiConsumer<NeedRequestService, NeedRequestForm> createNewNeedRequestConsumer =
            (service, needRequestForm) -> service.createNeedRequest(needRequestForm.getName(),
                    needRequestForm.getLocation(), needRequestForm.getPhone(), needRequestForm.getTypeOfHelp());

    BiConsumer<NeedRequestService, NeedRequestForm> updateNeedRequestConsumer =
            (service, needRequestForm) -> service.updateNeedRequest(needRequestForm.getName(),
                    needRequestForm.getLocation(),
                    needRequestForm.getPhone(), needRequestForm.getTypeOfHelp(), needRequestForm.getUuid());

    @Autowired
    public NeedRequestController(NeedRequestService needRequestService, NeedRequestFilteringService needRequestFilteringService) {
        this.needRequestService = needRequestService;
        this.needRequestFilteringService = needRequestFilteringService;
    }

    @PostMapping("/filtering")
    public String filtering(@ModelAttribute(FILTER_FORM_ATTR) NeedRequestFilterForm form,
                            RedirectAttributes redirectAttributes) {
        redirectAttributes.addAllAttributes(needRequestFilteringService.createFilteringRedirectAttributes(form));
        return REDIRECT_NEED_REQUEST_ALL;
    }

    @PostMapping("/submit-new-form")
    public String submitNeedRequestForm(@Valid @ModelAttribute(NEW_NEED_REQUEST_ATTR) NeedRequestForm needRequestForm,
                                        BindingResult br,
                                        @RequestParam Map<String, String> values,
                                        RedirectAttributes redirectAttributes) {

        if (br.hasErrors()) {
            return processFormWithErrors(redirectAttributes, values,
                    Map.of(NEW_HAS_ERRORS_ATTR, true, NEW_NEED_REQUEST_ATTR, needRequestForm,
                            BindingResult.MODEL_KEY_PREFIX + NEW_NEED_REQUEST_ATTR, br));
        } else {
            return processFormWithoutErrors(needRequestForm, redirectAttributes, values, createNewNeedRequestConsumer);
        }
    }

    @PostMapping("/edit-need-request")
    public String editNeedRequestForm(@Valid @ModelAttribute(EDIT_NEED_REQUEST_ATTR) NeedRequestForm needRequestForm,
                                      BindingResult br,
                                      @RequestParam Map<String, String> requestValues,
                                      RedirectAttributes redirectAttributes) {

        if (br.hasErrors()) {
            return processFormWithErrors(redirectAttributes, requestValues,
                    Map.of(HAS_ERRORS_ATTR, true, EDIT_NEED_REQUEST_ATTR, needRequestForm,
                            BindingResult.MODEL_KEY_PREFIX + EDIT_NEED_REQUEST_ATTR, br));
        } else {
            return processFormWithoutErrors(needRequestForm, redirectAttributes, requestValues, updateNeedRequestConsumer);
        }
    }

    private String processFormWithoutErrors(NeedRequestForm needRequestForm,
                                            RedirectAttributes redirectAttributes,
                                            Map<String, String> requestValues,
                                            BiConsumer<NeedRequestService, NeedRequestForm> consumer) {

        consumer.accept(needRequestService, needRequestForm);
        redirectAttributes.addAllAttributes(needRequestFilteringService.filterAttributes(requestValues));
        return REDIRECT_NEED_REQUEST_ALL;
    }

    private String processFormWithErrors(RedirectAttributes redirectAttributes,
                                         Map<String, String> requestValues,
                                         Map<String, ?> errorRelatedEntries) {

        errorRelatedEntries.forEach(redirectAttributes::addFlashAttribute);
        redirectAttributes.addAllAttributes(needRequestFilteringService.filterAttributes(requestValues));
        return REDIRECT_NEED_REQUEST_ALL;
    }

    @GetMapping(value = "/all")
    public String printAllNeedRequest(Model model,
                                      @RequestParam Map<String, String> values) {

        NeedRequestForm newForm = (NeedRequestForm) model.asMap().get(NEW_NEED_REQUEST_ATTR);
        model.addAttribute(NEW_NEED_REQUEST_ATTR, newForm == null ? new NeedRequestForm() : newForm);

        NeedRequestForm editForm = (NeedRequestForm) model.asMap().get(EDIT_NEED_REQUEST_ATTR);
        model.addAttribute(EDIT_NEED_REQUEST_ATTR, editForm == null ? new NeedRequestForm() : editForm);

        NeedRequestFilterForm needRequestFilterForm = needRequestFilteringService.addFilteringForm(values);
        model.addAttribute(FILTER_FORM_ATTR, needRequestFilterForm);
        addCommonModelAttributes(model, needRequestFilteringService.filterAttributes(values),
                needRequestFilterForm);
        return NEED_REQUEST_LIST_VIEW;
    }

    private void addCommonModelAttributes(Model model, Map<String, String> originalValues, NeedRequestFilterForm needRequestFilterForm) {
        model.addAttribute(EDIT_FORM_ACTION_URL, EDIT_NEED_REQUEST_URL);
        model.addAttribute(NEW_FORM_ACTION_URL, SUBMIT_NEW_NEED_REQUEST_URL);
        model.addAttribute(TYPES_ATTR, needRequestService.getTypesOfHelp());
        model.addAttribute(STATUSES_OF_HELP_ATTR, needRequestService.getHelpStatuses());
        model.addAttribute(NEED_REQUESTS_LIST_ATTR, needRequestService.getRequestFilteredList(needRequestFilterForm));
        originalValues.entrySet().stream()
                .forEach(entry -> model.addAttribute(entry.getKey(), entry.getValue()));
    }

    @GetMapping("/associate-to-volunteer")
    public String associateNeedRequestToVolunteer() {
        return getTestViewWithPageUnderConstructionMessage();
    }

    @GetMapping("/add-comment")
    public String addCommentToNeedRequest() {
        return getTestViewWithPageUnderConstructionMessage();
    }

    @GetMapping("/browse-history")
    public String browseHistoryOfNeedRequest() {
        return getTestViewWithPageUnderConstructionMessage();
    }

    private String getTestViewWithPageUnderConstructionMessage() {
        return REDIRECT_NEED_REQUEST_ALL;
    }
}