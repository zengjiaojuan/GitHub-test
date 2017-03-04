package com.cddgg.p2p.huitou.entity;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.cddgg.p2p.huitou.constant.NativeQuery;

/**
 * CreditorLink entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "creditor_link")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "creditorRoom",
        "userbasicsinfo" })
@NamedNativeQueries(value = {
        @NamedNativeQuery(name = NativeQuery.INIT_MATCH_COUNT, resultSetMapping = NativeQuery.INIT_MATCH_COUNT, query = "SELECT COUNT(*) count,userbasic_id FROM ( SELECT c.userbasic_id FROM creditor_link a, creditor_room b, creditor c WHERE c.status=:status AND a.creditor_id NOT IN (:ids) AND a.creditor_room_id = b.id AND a.creditor_id = c.id AND ( a.id_frost IS NULL OR ( a.id_frost =:id_frost OR a.time_frost <:time_frost )) AND b.cur_time >:cur_time_1 AND b.cur_time <=:cur_time_2 GROUP BY c.userbasic_id, b.id HAVING SUM(a.balance) >=:balance ) d GROUP BY d.userbasic_id"),
        @NamedNativeQuery(name = NativeQuery.PICK_CREDITOR, resultSetMapping = NativeQuery.PICK_CREDITOR, query = "SELECT a.id, a.creditor_id, a.balance, b.cur_time FROM creditor_link a LEFT JOIN creditor_room b ON a.creditor_room_id = b.id LEFT JOIN creditor c ON a.creditor_id = c.id WHERE c.status =:status AND c.userbasic_id =:userbasic_id AND a.creditor_id NOT IN (:ids) AND ( a.id_frost IS NULL OR ( a.id_frost =:id_frost OR a.time_frost <:time_frost )) AND b.cur_time >:cur_time_1 AND b.cur_time <=:cur_time_2 ORDER BY b.cur_time, a.balance ASC"),
        @NamedNativeQuery(name = NativeQuery.PICK_BIG_CREDITOR, resultSetMapping = NativeQuery.PICK_BIG_CREDITOR, query = "SELECT a.creditor_room_id FROM creditor_link a LEFT JOIN creditor_room b ON a.creditor_room_id = b.id LEFT JOIN creditor c ON a.creditor_id = c.id WHERE c.status =:status AND c.userbasic_id = :userbasic_id AND a.creditor_id NOT IN (:ids)( a.id_frost IS NULL OR ( a.id_frost =:id_frost OR a.time_frost <:time_frost )) AND a.balance = :balance AND b.cur_time > :cur_time_1 AND b.cur_time <= :cur_time_2 GROUP BY a.creditor_room_id HAVING COUNT(*) >= :num"),
        @NamedNativeQuery(name = NativeQuery.MATCH_INCOMPLETE_CREDITOR, resultSetMapping = NativeQuery.PICK_BIG_CREDITOR, query = "SELECT a.creditor_room_id FROM creditor_link a, creditor_room b, creditor c WHERE c.userbasic_id = :userbasic_id AND c.status =:status AND b.id NOT IN(:cur_ids) AND a.creditor_id NOT IN (:ids) AND a.creditor_room_id = b.id AND a.creditor_id = c.id AND ( a.id_frost IS NULL OR ( a.id_frost =:id_frost OR a.time_frost <:time_frost )) AND b.cur_time >:cur_time_1 AND b.cur_time <=:cur_time_2 GROUP BY c.userbasic_id, b.id HAVING SUM(a.balance) >=:balance"),
        @NamedNativeQuery(name = NativeQuery.PICK_INCOMPLETE_CREDITOR, resultSetMapping = NativeQuery.PICK_CREDITOR, query = "SELECT a.id, a.creditor_id, a.balance, b.cur_time FROM creditor_link a LEFT JOIN creditor_room b ON a.creditor_room_id = b.id LEFT JOIN creditor c ON a.creditor_id = c.id WHERE c.status =:status AND c.userbasic_id =:userbasic_id AND a.creditor_id NOT IN (:ids) AND ( a.id_frost IS NULL OR ( a.id_frost =:id_frost OR a.time_frost <:time_frost )) AND a.creditor_room_id IN(:creditor_room_ids) ORDER BY b.cur_time, a.balance ASC") })
@SqlResultSetMappings(value = {
        @SqlResultSetMapping(name = NativeQuery.INIT_MATCH_COUNT, entities = {}, columns = {
                @ColumnResult(name = "count"),
                @ColumnResult(name = "userbasic_id") }),
        @SqlResultSetMapping(name = NativeQuery.PICK_CREDITOR, entities = {}, columns = {
                @ColumnResult(name = "id"),
                @ColumnResult(name = "creditor_id"),
                @ColumnResult(name = "balance"),
                @ColumnResult(name = "cur_time") }),
        @SqlResultSetMapping(name = NativeQuery.PICK_BIG_CREDITOR, entities = {}, columns = { @ColumnResult(name = "creditor_room_id") }) })
public class CreditorLink implements java.io.Serializable {

    // Fields

    /**
     * id
     */
    private Long id;

    /**
     * 债券库
     */
    private CreditorRoom creditorRoom;

    /**
     * 债权
     */
    private Creditor creditor;

    /**
     * 债券信息表
     */
    private Double balance;

    /**
     * 冻结时间[精确到分钟]
     */
    private String timeFrost;

    /**
     * 冻结期间可访问id
     */
    private Userbasicsinfo userbasicsinfo;

    // Constructors

    /** default constructor */
    public CreditorLink() {
    }

    /**
     * creditor_id
     * 
     * @param creditorId
     *            creditor_id
     * @param creditorRoomId
     *            creditorRoom_id
     * @param balance
     *            balance
     */
    public CreditorLink(Long creditorId, Long creditorRoomId, Double balance) {
        creditor = new Creditor();
        creditor.setId(creditorId);

        creditorRoom = new CreditorRoom();
        creditorRoom.setId(creditorRoomId);

        this.balance = balance;
    }

    /**
     * full constructor
     * 
     * @param creditorRoom
     *            creditorRoom
     * @param creditor
     *            creditor
     */
    public CreditorLink(CreditorRoom creditorRoom, Creditor creditor) {
        this.creditorRoom = creditorRoom;
        this.creditor = creditor;
    }

    /**
     * Property accessors
     * 
     * @return id
     */
    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    /**
     * id
     * 
     * @param id
     *            id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * creditorRoom
     * 
     * @return creditorRoom
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creditor_room_id", nullable = false)
    public CreditorRoom getCreditorRoom() {
        return this.creditorRoom;
    }

    /**
     * creditorRoom
     * 
     * @param creditorRoom
     *            creditorRoom
     */
    public void setCreditorRoom(CreditorRoom creditorRoom) {
        this.creditorRoom = creditorRoom;
    }

    /**
     * creditor
     * 
     * @return creditor
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creditor_id", nullable = false)
    public Creditor getCreditor() {
        return this.creditor;
    }

    /**
     * creditor
     * 
     * @param creditor
     *            creditor
     */
    public void setCreditor(Creditor creditor) {
        this.creditor = creditor;
    }

    /**
     * balance
     * 
     * @return balance
     */
    @Column(name = "balance", nullable = false, precision = 20, scale = 4)
    public Double getBalance() {
        return balance;
    }

    /**
     * balance
     * 
     * @param balance
     *            balance
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    /**
     * timeFrost
     * 
     * @return timeFrost
     */
    @Column(name = "time_frost", nullable = true, length = 20)
    public String getTimeFrost() {
        return timeFrost;
    }

    /**
     * timeFrost
     * 
     * @param timeFrost
     *            timeFrost
     */
    public void setTimeFrost(String timeFrost) {
        this.timeFrost = timeFrost;
    }

    /**
     * userbasicsinfo
     * 
     * @return userbasicsinfo
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_frost", nullable = true)
    public Userbasicsinfo getUserbasicsinfo() {
        return this.userbasicsinfo;
    }

    /**
     * userbasicsinfo
     * 
     * @param userbasicsinfo
     *            userbasicsinfo
     */
    public void setUserbasicsinfo(Userbasicsinfo userbasicsinfo) {
        this.userbasicsinfo = userbasicsinfo;
    }

}