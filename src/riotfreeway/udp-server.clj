(ns riotfreeway.udp-server
  (:import (java.net DatagramSocket DatagramPacket))
  (:gen-class))

(def socket (DatagramSocket. 9001))

(def running(atom true))
(def buffer(make-array Byte/TYPE 1024))

(defn get-and-parse [packet]
  (new String (.getData packet) 0 (.getLength packet)))
;;  (println (String. (.getData packet))))

(defn start-receiver []
  (while (true? @running)
    (let [packet (DatagramPacket. buffer 1024)]
      (do
        (.receive socket packet))
        ;;(future (parse packet)))
      )
    )
  )
