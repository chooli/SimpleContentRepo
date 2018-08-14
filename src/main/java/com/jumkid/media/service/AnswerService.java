package com.jumkid.media.service;

import com.jumkid.media.model.Answer;
import com.jumkid.media.model.dao.IAnswerRepository;
import com.jumkid.media.util.Constants;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Services for answers
 */

@Configuration
@Service("answerService")
@EnableElasticsearchRepositories(basePackages = "com.jumkid.media.model")
public class AnswerService extends AbstractCommonService{

    @Autowired
    private IAnswerRepository answerRepository;

    @Autowired
    private ElasticsearchTemplate template;


    @Override
    public ServiceCommand execute(ServiceCommand cmd) {
        try{
            if(isAction(cmd, Constants.SERVICE_ACTION_FIND)){
                String id = (String)cmd.getParam("id");

                if(id!=null){
                    cmd.addResult("answer", getAnswer(id));
                }
            }else
            if(isAction(cmd, Constants.SERVICE_ACTION_SAVE)){

                Answer answer = (Answer)cmd.getParam("answer");

                if(answer!=null){
                    answer = saveAnswer(answer);
                    cmd.addResult("answer", answer);
                }

            }else
            if(isAction(cmd, Constants.SERVICE_ACTION_UPDATE)){
                Answer answer = (Answer)cmd.getParam("answer");

                if(answer!=null){
                    boolean isUpdated = updateAnswer(answer);
                    cmd.addResult("success", isUpdated);
                }
            }else
            if(isAction(cmd, Constants.SERVICE_ACTION_DELETE)){
                String id = (String)cmd.getParam("id");

                if(id!=null){
                    boolean isDeleted = deleteAnswer(id);
                    cmd.addResult("success", isDeleted);
                }
            }else
            if(isAction(cmd, Constants.SERVICE_ACTION_LIST)){
                cmd.addResult("answers", this.getAnswers());
            }

        }catch(Exception e){
            e.printStackTrace();
            cmd.addError("answer-service", e.getMessage());
        }

        return cmd;
    }

    /**
     *
     * @param answer
     * @return
     */
    private Answer saveAnswer(Answer answer){
        template.putMapping(Answer.class);
        final IndexQuery indexQuery = new IndexQuery();
        indexQuery.setObject(answer);
        template.index(indexQuery);
        template.refresh(Answer.class);
        answerRepository.save(answer);

        return answer;
    }

    /**
     *
     * @param answer
     * @return
     */
    private boolean updateAnswer(Answer answer){
        try{
            final UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.index("answer");
            updateRequest.type("doc");
            updateRequest.id(answer.getId());
            updateRequest.doc(XContentFactory.jsonBuilder().startObject()
                    .field("author", answer.getAuthor())
                    .field("title", answer.getTitle())
                    .field("colorCode", answer.getColorCode())
                    .field("representedQuestion", answer.getRepresentedQuestion())
                    .field("response", answer.getResponse())
                    .endObject());
            final UpdateQuery updateQuery = new UpdateQueryBuilder().withId(answer.getId())
                    .withClass(Answer.class).withUpdateRequest(updateRequest).build();
            UpdateResponse updateResponse =  template.update(updateQuery);

            return updateResponse.getResult().getLowercase().equals("updated");
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @return
     */
    private Answer getAnswer(String id){
        final GetQuery idQuery = new GetQuery();
        idQuery.setId(id);
        Answer answer = template.queryForObject(idQuery, Answer.class);

        return answer;
    }

    /**
     *
     * @return
     */
    private List<Answer> getAnswers(){
        SearchQuery allQuery = new NativeSearchQueryBuilder()
                .withFilter(QueryBuilders.matchAllQuery())
                .build();
        //CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria());
        List<Answer> answers = template.queryForList(allQuery, Answer.class);

        return answers;
    }

    /**
     *
     * @param id
     * @return
     */
    private boolean deleteAnswer(String id){
        template.delete(Answer.class, id);
        template.refresh(Answer.class);

        return true;
    }

    public void setTemplate(ElasticsearchTemplate template) {
        this.template = template;
    }

    public void setAnswerRepository(IAnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }
}
