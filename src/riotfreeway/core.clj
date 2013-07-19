(ns riotfreeway.core
  (:import [backtype.storm StormSubmitter LocalCluster])
  (:use [backtype.storm clojure config])
  (:require [riotfreeway.udp-server :as udp-server])
  (:gen-class))

(defspout socket-spout ["entry"]
  [conf context collector]
    (spout
     (nextTuple []
       (Thread/sleep 100)
       (while true (emit-spout! collector [(context)])))))

(defbolt print-bolt ["message"] [str message]
    (println (message))
    (emit-bolt! message))
    ;;(ack! message))

(defn mk-topology []
  (topology
    {"1" (spout-spec socket-spout)}
    {"2" (bolt-spec {"1" :shuffle} print-bolt)}))

(defn run-local! []
  (let [cluster (LocalCluster.)]
    (.submitTopology cluster "RiotFreeWay" {TOPOLOGY-DEBUG true} (mk-topology))
    (Thread/sleep 10000)
;;    (.shutdown cluster)
    ))

(defn submit-topology! [name]
  (StormSubmitter/submitTopology
   name
   {TOPOLOGY-DEBUG true
    TOPOLOGY-WORKERS 4}
   (mk-topology)))

(defn -main
  (start-receiver)
  ([]
    (run-local!))
  ([name]
    (submit-topology! name)))
