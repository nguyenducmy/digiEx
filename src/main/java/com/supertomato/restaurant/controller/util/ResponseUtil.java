package com.supertomato.restaurant.controller.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @author DiGiEx
 */
@Component
public class ResponseUtil {
    private ObjectMapper mapper;

    @Autowired
    public ResponseUtil(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Create HTTP Response
     * @param apiStatus
     * @param data
     * @return
     */
    private RestAPIResponse _createResponse(APIStatus apiStatus, Object data) {
        return new RestAPIResponse(apiStatus, data);
    }

    /**
     * Create HTTP Response with description
     * @param apiStatus
     * @param data
     * @return
     */
    private RestAPIResponse _createResponse(APIStatus apiStatus, Object data, String description) {
        return new RestAPIResponse(apiStatus, data, description);
    }

    /**
     * Build HTTP Response
     * @param apiStatus
     * @param data
     * @param httpStatus
     * @return
     */
    public ResponseEntity<RestAPIResponse> buildResponse(APIStatus apiStatus, Object data, HttpStatus httpStatus) {
        return new ResponseEntity(_createResponse(apiStatus, data), httpStatus);
    }

    /**
     * Build HTTP Response with description
     * @param apiStatus
     * @param data
     * @param description
     * @param httpStatus
     * @return
     */
    public ResponseEntity<RestAPIResponse> buildResponse(APIStatus apiStatus, Object data, String description, HttpStatus httpStatus) {
        return new ResponseEntity(_createResponse(apiStatus, data, description), httpStatus);
    }

    /**
     * Return success HTTP Request
     * @param data
     * @return
     */
    public ResponseEntity<RestAPIResponse> successResponse(Object data) {
        return buildResponse(APIStatus.OK, data, HttpStatus.OK);
    }

    /**
     * Return success HTTP Request with description
     * @param data
     * @param description
     * @return
     */
    public ResponseEntity<RestAPIResponse> successResponse(Object data, String description) {
        return buildResponse(APIStatus.OK, data, description, HttpStatus.OK);
    }
}
