package com.infoshare.service;

import com.infoshare.TestDataUtils;
import com.infoshare.database.DB;
import com.infoshare.domain.NeedRequest;
import com.infoshare.domain.TypeOfHelp;
import com.infoshare.dto.NeedRequestFilterForm;
import com.infoshare.dto.NeedRequestListObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class NeedRequestServiceTest {
    private static final DB db = mock(DB.class);
    private static final ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
    private static final NeedRequestService needRequestService = new NeedRequestService(db);

    private final ArgumentCaptor<NeedRequest> needRequestArgumentCaptor = ArgumentCaptor.forClass(NeedRequest.class);

    @BeforeAll
    static void setUp(){
        needRequestService.setApplicationEventPublisher(applicationEventPublisher);
        doNothing().when(applicationEventPublisher).publishEvent(isA(Object.class));
    }

    @Test
    void shouldCreateNeedRequest() {
        doNothing().when(db).saveNeedRequest(needRequestArgumentCaptor.capture());
        // GIVEN
        String name = "Ania";
        String location = "Gdansk";
        String phone = "122122122";
        // WHEN
        needRequestService.createNeedRequest(name, location, phone, TypeOfHelp.HOUSE_HELP);

        // THEN
        NeedRequest needRequest1 = needRequestArgumentCaptor.getValue();
        assertThat(needRequest1.getPersonInNeed().getName()).isEqualTo(name);
        assertThat(needRequest1.getPersonInNeed().getLocation()).isEqualTo(location);
        assertThat(needRequest1.getPersonInNeed().getPhone()).isEqualTo(phone);
    }

    @Test
    void shouldGetAllNeedRequest() throws Exception {
        //GIVEN
        List<NeedRequestListObject> allExpectedNeedRequests = TestDataUtils.getNeedRequestListObject("NeedRequestListObject.json");
        when(db.getNeedRequests()).thenReturn(TestDataUtils.getNeedRequests());
        NeedRequestFilterForm needRequestFilterForm = new NeedRequestFilterForm();

        //WHEN
        List<NeedRequestListObject> needRequestListObjects = needRequestService.getRequestFilteredList(needRequestFilterForm);

        //THEN
        assertThat(needRequestListObjects).hasSize(allExpectedNeedRequests.size()).containsAll(allExpectedNeedRequests);
    }

    @Test
    void shouldGetNeedRequestsWithLocation() throws Exception {

        //GIVEN
        List<NeedRequestListObject> allNeedRequests = TestDataUtils.getNeedRequestListObject("locationTestObjects.json");
        when(db.getNeedRequests()).thenReturn(TestDataUtils.getNeedRequests());
        NeedRequestFilterForm needRequestFilterForm = NeedRequestFilterForm.FilterFormBuilder.aFilterForm().withLocation("Gdynia").build();

        //WHEN
        List<NeedRequestListObject> needRequestListObjects = needRequestService.getRequestFilteredList(needRequestFilterForm);

        // THEN
        assertThat(needRequestListObjects).hasSize(1).containsAll(allNeedRequests);
    }

    @Test
    void shouldGetNeedRequestsWithStartAndEndData() throws Exception {

        //GIVEN
        List<NeedRequestListObject> expectedResult = TestDataUtils.getNeedRequestListObject("datesTestObjects.json");
        when(db.getNeedRequests()).thenReturn(TestDataUtils.getNeedRequests());
        NeedRequestFilterForm needRequestFilterForm = NeedRequestFilterForm.FilterFormBuilder.aFilterForm().withStartDate("2021-02-02").withEndDate("2021-02-03").build();

        //WHEN
        List<NeedRequestListObject> needRequestListObjects = needRequestService.getRequestFilteredList(needRequestFilterForm);

        // THEN
        assertThat(needRequestListObjects).hasSize(21).containsAll(expectedResult);
    }

    @Test
    void shouldGetNeedRequestsWithHelpStatuses() throws Exception {

        //GIVEN
        List<NeedRequestListObject> expectedResult = TestDataUtils.getNeedRequestListObject("helpStatusesTestObjects.json");
        when(db.getNeedRequests()).thenReturn(TestDataUtils.getNeedRequests());
        NeedRequestFilterForm needRequestFilterForm = NeedRequestFilterForm.FilterFormBuilder.aFilterForm().withHelpStatuses("DONE").build();

        //WHEN
        List<NeedRequestListObject> needRequestListObjects = needRequestService.getRequestFilteredList(needRequestFilterForm);

        //THEN
        assertThat(needRequestListObjects).hasSize(4).containsAll(expectedResult);
    }

    @Test
    void shouldGetNeedRequestsWithTypeOfHelp() throws Exception {
        //GIVEN
        List<NeedRequestListObject> expectedResult = TestDataUtils.getNeedRequestListObject("typesOfHelpTestObjects.json");
        when(db.getNeedRequests()).thenReturn(TestDataUtils.getNeedRequests());
        NeedRequestFilterForm needRequestFilterForm = NeedRequestFilterForm.FilterFormBuilder.aFilterForm().withTypeOfHelps("SHOPPING").build();

        //WHEN
        List<NeedRequestListObject> needRequestListObjects = needRequestService.getRequestFilteredList(needRequestFilterForm);

        //THEN
        assertThat(needRequestListObjects).hasSize(25).containsAll(expectedResult);
    }
}
