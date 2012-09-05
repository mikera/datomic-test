(ns dt.datomic-test
  (:use [datomic.api :only [db q] :as d]))

(def uri "datomic:mem://test")

(defn create-db []
  (d/delete-database uri)
  (d/create-database uri)

  ;; add a name attribute
  (d/transact conn [{:db/id #db/id[:db.part/db]
                     :db/ident :person/name
                     :db/valueType :db.type/string
                     :db/cardinality :db.cardinality/one
                     :db/doc "A person's name"
                     :db.install/_attribute :db.part/db}])

  ;; add a name attribute
	(d/transact conn [{:db/id #db/id[:db.part/db]
	                   :db/ident :person/age
	                   :db/valueType :db.type/long
	                   :db/cardinality :db.cardinality/one
	                   :db/doc "A person's Age"
	                   :db.install/_attribute :db.part/db}])
  )

(create-db)

(def conn (d/connect uri))

(d/transact conn [{:db/id #db/id[:db.part/db]
                   :db/ident :person/name
                   :db/valueType :db.type/string
                   :db/cardinality :db.cardinality/one
                   :db/doc "A person's name"
                   :db.install/_attribute :db.part/db}])

(d/transact conn [{:db/id #db/id[:db.part/db]
                   :db/ident :person/age
                   :db/valueType :db.type/long
                   :db/cardinality :db.cardinality/one
                   :db/doc "A person's Age"
                   :db.install/_attribute :db.part/db}])

(defn add-person [name age]
  (d/transact conn [{:db/id #db/id[:db.part/user] 
                     :person/name name
                     :person/age age}]))

	;; Lots of sample code to play with
	(comment  
	  
	(add-person "Mike" 34)
	(add-person "Bob" 27)
	(add-person "Rachel" 23)
	
	;; find all names
	(q '[:find ?n :where [?c :person/name ?n]] (db conn))
	
	;; find names and ages
	(q '[:find ?n ?a  :where [?c :person/name ?n] [?c :person/age ?a]] (db conn))
	
	;; people with ahes less than 30
	(q '[:find ?n :where [?c :person/name ?n] [?c :person/age ?a] [(< ?a 30)]] (db conn))
	
	;; find all attributes
	 (q '[:find ?name :where [_ :db.install/attribute ?a] [?a :db/ident ?name]] (db conn))
	
	;; add a person to a temp database
	(def dd (:db-after @(add-person "Val" 32)))
	(q '[:find ?n :where [?c :person/name ?n]] dd)
	
  ;; serach including transaction time
	(q '[:find ?n ?t :where [?c :person/name ?n ?t]] (db conn))  
)

 (create-db)