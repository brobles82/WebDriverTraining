package com.octopus.eventhandlers.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.octopus.decoratorbase.AutomatedBrowserBase;
import com.octopus.eventhandlers.EventHandler;
import io.kubernetes.client.ApiClient;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.util.Config;

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;

public class SaveKubernetesConfigMap implements EventHandler {
    public static final String KUBERNETES_URL = "Kubernetes-Url";
    public static final String KUBERNETES_TOKEN = "Kubernetes-Token";
    public static final String KUBERNETES_NAMESPACE = "Kubernetes-Namespace";
    public static final String KUBERNETES_CONFIGMAP = "Kubernetes-ConfigMap";
    private static final DecimalFormat df = new DecimalFormat("#.##");
    private static final String UI_AVERAGE_KEY = "ui-test-avg";
    private static final String UI_EXE_TIME_KEY = "ui-test-exe-time";

    @Override
    public Map<String, String> finished(final String id,
                                        final boolean status,
                                        final String featureFile,
                                        final String content,
                                        final Map<String, String> headers,
                                        final Map<String, String> previousResults) {
        if (!headers.containsKey(KUBERNETES_URL) ||
                !headers.containsKey(KUBERNETES_TOKEN) ||
                !headers.containsKey(KUBERNETES_NAMESPACE) ||
                !headers.containsKey(KUBERNETES_CONFIGMAP)) {
            System.out.println("The " +
                    KUBERNETES_URL + ", " +
                    KUBERNETES_TOKEN + ", " +
                    KUBERNETES_NAMESPACE + " and " +
                    KUBERNETES_CONFIGMAP +
                    " headers must be defined to save the results into a config map");
            return previousResults;
        }

        System.out.println(
                "Attempting to update configmap " + headers.get(KUBERNETES_CONFIGMAP) +
                        " in namespace " + headers.get(KUBERNETES_NAMESPACE) +
                        " in cluster " + headers.get(KUBERNETES_URL));

        final ApiClient client = Config.fromToken(
                headers.get(KUBERNETES_URL),
                headers.get(KUBERNETES_TOKEN),
                false);
        Configuration.setDefaultApiClient(client);

        final String result = status ? df.format(AutomatedBrowserBase.getAverageWaitTime() / 1000) : "";
        final String averageTime = "{\"op\":\"add\",\"path\":\"/data/" + UI_AVERAGE_KEY + "\"," +
                "\"value\":\"" + result + "\"}";
        applyPatch(averageTime, headers);

        final String testTime = "{\"op\":\"add\",\"path\":\"/data/" + UI_EXE_TIME_KEY + "\"," +
                "\"value\":\"" + Instant.now().getEpochSecond() + "\"}";
        applyPatch(testTime, headers);

        return previousResults;
    }

    private void applyPatch(final String json, final Map<String, String> headers) {
        try {
            final CoreV1Api api = new CoreV1Api();

            final ArrayList<JsonObject> arr = new ArrayList<>();
            arr.add(((JsonElement) deserialize(
                    json,
                    JsonElement.class)).getAsJsonObject());

            api.patchNamespacedConfigMap(
                    headers.get(KUBERNETES_CONFIGMAP),
                    headers.get(KUBERNETES_NAMESPACE),
                    arr,
                    "false"
            );
        } catch (final Exception ex) {
            System.out.println("Failed to send result to Kubernetes.\n" + ex.toString());
        }
    }

    public Object deserialize(final String jsonStr, final Class<?> targetClass) {
        final Object obj = (new Gson()).fromJson(jsonStr, targetClass);
        return obj;
    }

}