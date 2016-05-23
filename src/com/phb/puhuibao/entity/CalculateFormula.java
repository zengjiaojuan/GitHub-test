package com.phb.puhuibao.entity;


import com.idp.pub.entity.annotation.MetaTable;

/**
 * @author wei
 *
 */
@MetaTable
public class CalculateFormula implements java.io.Serializable {
	private static final long serialVersionUID = 5736511926578194639L;
	
	private String formulaId;//``formula_id``
	private String formulaText;//```formula_text```

	public String getFormulaId() {
		return formulaId;
	}
	public void setFormulaId(String formulaId) {
		this.formulaId = formulaId;
	}
	public String getFormulaText() {
		return formulaText;
	}
	public void setFormulaText(String formulaText) {
		this.formulaText = formulaText;
	}
 
 
	
	 

}
