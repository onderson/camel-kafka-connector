/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.camel.kafkaconnector.services.aws;

import java.util.Properties;
import java.util.function.Supplier;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import org.apache.camel.kafkaconnector.clients.aws.AWSConfigs;
import org.apache.camel.kafkaconnector.clients.aws.sqs.AWSSQSClient;
import org.apache.camel.kafkaconnector.clients.aws.sqs.TestAWSCredentialsProvider;

public class AWSRemoteService<T> implements AWSService<T> {
    private static final AWSCredentialsProvider CREDENTIALS_PROVIDER = new TestAWSCredentialsProvider();
    private Supplier<T> remoteClientSupplier;

    public AWSRemoteService(Supplier<T> remoteClientSupplier) {
        this.remoteClientSupplier = remoteClientSupplier;
    }


    @Override
    public T getClient() {
        return remoteClientSupplier.get();
    }

    @Override
    public AWSCredentials getCredentials() {
        return CREDENTIALS_PROVIDER.getCredentials();
    }

    @Override
    public Properties getConnectionProperties() {
        Properties properties = new Properties();

        AWSCredentials credentials = getCredentials();

        properties.put(AWSConfigs.ACCESS_KEY, credentials.getAWSAccessKeyId());
        properties.put(AWSConfigs.SECRET_KEY, credentials.getAWSSecretKey());
        properties.put(AWSConfigs.REGION, Regions.US_EAST_1.name());

        return properties;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void shutdown() {

    }

    public static AWSSQSClient newSQSClient() {
        AmazonSQS sqs = AWSClientUtils.newSQSClient();

        return new AWSSQSClient(sqs);
    }


}
