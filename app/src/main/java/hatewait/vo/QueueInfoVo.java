package hatewait.vo;

import java.io.Serializable;

public class QueueInfoVo implements Serializable{
	String id;
	int phone;
	String name;
	int peopleNum;
	int turn;
	@Override
	public String toString() {
		return "QueueInfoVo [id=" + id + ", phone=" + phone + ", name=" + name + ", peopleNum=" + peopleNum + ", turn="
				+ turn + "]";
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getPhone() {
		return phone;
	}
	public void setPhone(int phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPeopleNum() {
		return peopleNum;
	}
	public void setPeopleNum(int peopleNum) {
		this.peopleNum = peopleNum;
	}
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
}
