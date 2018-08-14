package com.jumkid.media;

import com.jumkid.media.model.Answer;
import com.jumkid.media.model.dao.IAnswerRepository;
import com.jumkid.media.service.AnswerService;
import com.jumkid.media.service.ServiceCommand;
import com.jumkid.media.util.Constants;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class AnswerServiceTest {

    private static String answerId = "AWMw8DmLj0yN0NOeEUH2";

    private Answer answer;

    private AnswerService service;

    @Mock
    private IAnswerRepository answerRepository;
    @Mock
    private ElasticsearchTemplate template;

    @Configuration
    static class ContextConfiguration {
        //void
    }

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        answer = new Answer();
        answer.setAuthor("chooli");
        answer.setTitle("test question");
        answer.setRepresentedQuestion("what is unit test?");
        answer.setColorCode("#FF0000");
        answer.setPostDate(new Date());
        answer.setResponse("<div>Hello, unit test!</div>");

        service = new AnswerService();

        service.setAnswerRepository(answerRepository);
        service.setTemplate(template);

    }

    @Test
    public void testCreate(){
        ServiceCommand cmd = new ServiceCommand();
        cmd.setAction(Constants.SERVICE_ACTION_SAVE);
        cmd.addParam("answer", answer);

        Mockito.when(template.index(any())).thenReturn(answerId);
        Mockito.when(answerRepository.save(any(Answer.class))).thenReturn(answer);

        cmd = service.execute(cmd);

        Answer newAnswer = (Answer) cmd.getResult("answer");
        assertNotNull(newAnswer);

    }

    @Test
    public void testUpdate(){
        ServiceCommand cmd = new ServiceCommand();
        cmd.setAction(Constants.SERVICE_ACTION_UPDATE);
        cmd.addParam("answer", answer);

        UpdateResponse mockUpdateResponse = mock(UpdateResponse.class);
        DocWriteResponse mockDocWriteResponse = mock(DocWriteResponse.class);
        Mockito.when(template.update(any(UpdateQuery.class))).thenReturn(mockUpdateResponse);
        Mockito.when(mockUpdateResponse.getResult()).thenReturn(DocWriteResponse.Result.UPDATED);

        cmd = service.execute(cmd);

        assert((boolean)cmd.getResult("success"));

    }

    @Test
    public void testFind(){
        ServiceCommand cmd = new ServiceCommand();
        cmd.setAction(Constants.SERVICE_ACTION_FIND);
        cmd.addParam("id", answerId);

        answer.setId(answerId);

        Mockito.when(template.queryForObject(any(GetQuery.class), any())).thenReturn(answer);

        cmd = service.execute(cmd);

        Answer updateAnswer = (Answer)cmd.getResult("answer");
        assertNotNull(updateAnswer);
        assert(updateAnswer.getId().equals(answerId));
    }

    @Test
    public void testDelete(){
        ServiceCommand cmd = new ServiceCommand();
        cmd.setAction(Constants.SERVICE_ACTION_DELETE);
        cmd.addParam("id", answerId);

        Mockito.when(template.delete(Answer.class, answerId)).thenReturn(answerId);

        cmd = service.execute(cmd);

        assert((boolean)cmd.getResult("success"));

    }

    @Test
    public void testList(){
        ServiceCommand cmd = new ServiceCommand();
        cmd.setAction(Constants.SERVICE_ACTION_LIST);

        List answers = new ArrayList<>();
        answers.add(answer);
        Mockito.when(template.queryForList(any(SearchQuery.class), any())).thenReturn(answers);

        cmd = service.execute(cmd);

        answers = (List<Answer>) cmd.getResult("answers");
        assert(answers.size()!=0);
    }

}
