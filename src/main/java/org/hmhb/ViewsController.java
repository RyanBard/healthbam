package org.hmhb;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;

@Controller
public class ViewsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewsController.class);

    private static final String INDEX_HTML_PATH = "/index.html";

    @Timed
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/views/**"
    )
    public String handleViewsSubpaths() {
        LOGGER.debug(
                "handleViewsSubpaths called: {}",
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE
        );
        return INDEX_HTML_PATH;
    }

    @Timed
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/views"
    )
    public String handleViews() {
        LOGGER.debug("handleViews called");
        return INDEX_HTML_PATH;
    }

    @Timed
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/"
    )
    public String handleRoot() {
        LOGGER.debug("handleRoot called");
        return INDEX_HTML_PATH;
    }

}
