package com.feilong.io.json.dify;

import org.junit.Test;

import com.feilong.io.IOReaderUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HotBonusAnalysisDifyApplyJsonTest{

    @Test
    public void test2(){
        String responseBody = IOReaderUtil.readToString("classpath:json/simpleJsonresult.txt");
        DifyCompletionResponseStringToBeanConverter<String> converter = new DifyCompletionResponseStringToBeanConverter<>(String.class);
        //        converter.setCustomClassMap(toMap("result", BonusAnalysisDifyResponseEntity.class));

        DifyCompletionResponse<String> bean = converter.convert(responseBody);

        //---------------------------------------------------------------

        DifyCompletionResponseData<String> data = bean.getData();
        String outputs = data.getOutputs();

        log.info("responseBody:[{}] outputs:[{}] bean:[{}]", responseBody, outputs, bean);
    }

    //---------------------------------------------------------------

    //    @Test
    //    public void test1(){
    //        String responseBody = IOReaderUtil.readToString("classpath:bonus/jsonresult.txt");
    //        DifyCompletionResponseStringToBeanConverter<BonusAnalysisDifyResponse> converter = new DifyCompletionResponseStringToBeanConverter<>(
    //                        BonusAnalysisDifyResponse.class);
    //        converter.setCustomClassMap(toMap("result", BonusAnalysisDifyResponseEntity.class));
    //
    //        DifyCompletionResponse<BonusAnalysisDifyResponse> bean = converter.convert(responseBody);
    //
    //        //---------------------------------------------------------------
    //
    //        DifyCompletionResponseData<BonusAnalysisDifyResponse> data = bean.getData();
    //        BonusAnalysisDifyResponse outputs = data.getOutputs();
    //
    //        log.info(" responseBody:[{}] outputs:[{}] bean:[{}]", responseBody, outputs, bean);
    //    }
    //
    //    @Test
    //    public void test1BonusAnalysisDifyStringResponse(){
    //
    //        String responseBody = IOReaderUtil.readToString("classpath:bonus/simpleJsonresult.txt");
    //
    //        DifyCompletionResponseStringToBeanConverter<BonusAnalysisDifyStringResponse> converter = new DifyCompletionResponseStringToBeanConverter<>(
    //                        BonusAnalysisDifyStringResponse.class);
    //        //        converter.setCustomClassMap(toMap("result", BonusAnalysisDifyResponseEntity.class));
    //
    //        DifyCompletionResponse<BonusAnalysisDifyStringResponse> bean = converter.convert(responseBody);
    //
    //        //---------------------------------------------------------------
    //
    //        DifyCompletionResponseData<BonusAnalysisDifyStringResponse> data = bean.getData();
    //        BonusAnalysisDifyStringResponse outputs = data.getOutputs();
    //
    //        log.info(" responseBody:[{}] result:[{}] outputs:[{}] bean:[{}]", responseBody, outputs.getResult(), outputs, bean);
    //    }

}
