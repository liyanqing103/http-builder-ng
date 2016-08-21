/**
 * Copyright (C) 2016 David Clark
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package groovyx.net.http;

import groovy.lang.Closure;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Provides the public interface used for configuring the HTTP Builder NG client.
 */
public interface HttpConfig {

    /**
     * Defines the allowed values of the response status.
     */
    enum Status { SUCCESS, FAILURE };

    /**
     * Defines the allowed values of the HTTP authentication type.
     */
    enum AuthType { BASIC, DIGEST };

    /**
     *  Defines the accessible HTTP authentication information.
     */
    interface Auth {

        /**
         * Retrieve the authentication type for the request.
         *
         * @return the AuthType for the Request
         */
        AuthType getAuthType();

        String getUser();

        String getPassword();

        /**
         * Configures the request to use BASIC authentication with the given username and password. The authentication will not be preemptive. This
         * method is an alias for calling: <code>basic(String, String, false)</code>.
         *
         * @param user the username
         * @param password the user's password
         */
        default void basic(String user, String password) {
            basic(user, password, false);
        }

        /**
         * Configures the request to use BASIC authentication with the given information.
         *
         * @param user the username
         * @param password the user's password
         * @param preemptive
         */
        void basic(String user, String password, boolean preemptive);

        /**
         * Configures the request to use DIGEST authentication with the given username and password. The authentication will not be preemptive. This
         * method is an alias for calling: <code>digest(String, String, false)</code>.
         *
         * @param user the username
         * @param password the user's password
         */
        default void digest(String user, String password) {
            digest(user, password, false);
        }

        /**
         * Configures the request to use DIGEST authentication with the given information.
         *
         * @param user the username
         * @param password the user's password
         * @param preemptive
         */
        void digest(String user, String password, boolean preemptive);
    }

    /**
     * Defines the accessible HTTP request information.
     */
    interface Request {

        /**
         * Retrieves the authentication information for the request.
         *
         * @return the authentication information for the request.
         */
        Auth getAuth();

        /**
         * Used to specify the <code>Content-type</code> header for the request.
         *
         * @param val the content type value to be used
         */
        void setContentType(String val);

        /**
         * Used to specify the content type character set to be used by the request.
         *
         * @param val the content type character set value to be used
         */
        void setCharset(String val);

        /**
         * Used to specify the content type character set to be used by the request.
         *
         * @param val the content type character set value to be used
         */
        void setCharset(Charset val);

        /**
         * Retrieves the <code>UriBuilder</code> for the request, which provides methods for more fine-grained URI specification.
         *
         * @return the UriBuilder for the request
         */
        UriBuilder getUri();

        /**
         * Used to specify the request URI as a String.
         *
         * @param val the URI to be used for the request, as a String
         */
        void setUri(String val) throws URISyntaxException;

        /**
         * Used to specify the request URI.
         *
         * @param val the URI to be used for the request.
         */
        void setUri(URI val);

        /**
         * Used to specify the request URI as a URL instance.
         *
         * @param val the URI to be used for the request, as a URL instance
         */
        void setUri(URL val) throws URISyntaxException;

        Map<String, String> getHeaders();

        void setHeaders(Map<String, String> toAdd);

        void setAccept(String[] values);

        void setAccept(List<String> values);

        /**
         * Used to specify the body content for the request. The request body content may be altered by configured encoders internally or may be
         * passed on unmodified.
         *
         * @param val the request body content
         */
        void setBody(Object val);

        /**
         * Adds a cookie to the request with the specified name and value, and no expiration date. This method is an alias for calling:
         * <code>cookie(String, String, null)</code>.
         *
         * @param name the cookie name
         * @param value the cookie value
         */
        default void cookie(String name, String value) {
            cookie(name, value, null);
        }

        /**
         * Adds a cookie to the request with the specified information.
         *
         * @param name the cookie name
         * @param value the cookie value
         * @param expires the expiration date of the cookie (<code>null</code> is allowed)
         */
        void cookie(String name, String value, Date expires);

        /**
         * Specifies the request encoder (ToServer instance) to be used when encoding the given content type.
         *
         * @param contentType the content type
         * @param val the request encoder (wrapped in a <code>BiConsumer</code> function)
         */
        void encoder(String contentType, BiConsumer<ChainedHttpConfig, ToServer> val);

        /**
         * Specifies the request encoder (ToServer instance) to be used when encoding the given list of content types.
         *
         * @param contentTypes the content types
         * @param val the request encoder (wrapped in a <code>BiConsumer</code> function)
         */
        void encoder(List<String> contentTypes, BiConsumer<ChainedHttpConfig, ToServer> val);

        /**
         * Retrieves the request encoder (ToServer instance) for the specified content type wrapped in a <code>BiConsumer</code> function.
         *
         * @param contentType the content type of the encoder to be retrieved
         * @return the encoder for the specified content type (wrapped in a <code>BiConsumer</code> function)
         */
        BiConsumer<ChainedHttpConfig, ToServer> encoder(String contentType);
    }

    /**
     * Defines the accessible HTTP response information.
     */
    interface Response {

        /**
         * FIXME: document
         */
        void when(Status status, Closure<Object> closure);

        /**
         * FIXME: document
         */
        void when(Integer code, Closure<Object> closure);

        /**
         * FIXME: document
         */
        void when(String code, Closure<Object> closure);

        /**
         * FIXME: document
         */
        Closure<Object> when(Integer code);

        /**
         * FIXME: document
         */
        void success(Closure<Object> closure);

        /**
         * FIXME: document
         */
        void failure(Closure<Object> closure);

        /**
         * FIXME: document
         */
        void parser(String contentType, BiFunction<ChainedHttpConfig, FromServer, Object> val);

        /**
         * FIXME: document
         */
        void parser(List<String> contentTypes, BiFunction<ChainedHttpConfig, FromServer, Object> val);

        /**
         * FIXME: document
         */
        BiFunction<ChainedHttpConfig, FromServer, Object> parser(String contentType);
    }

    /**
     * FIXME: document
     */
    void context(String contentType, Object id, Object obj);

    /**
     * FIXME: document
     */
    default void context(final List<String> contentTypes, final Object id, final Object obj) {
        for (String contentType : contentTypes) {
            context(contentType, id, obj);
        }
    }

    /**
     * Used to retrieve information about the HTTP request.
     *
     * @return the HTTP request
     */
    Request getRequest();

    /**
     * Used to retrieve information about the HTTP response.
     *
     * @return the HTTP response
     */
    Response getResponse();
}
