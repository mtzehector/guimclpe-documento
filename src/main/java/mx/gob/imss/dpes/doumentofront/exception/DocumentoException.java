/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.imss.dpes.doumentofront.exception;

import mx.gob.imss.dpes.common.exception.BusinessException;

/**
 *
 * @author eduardo.loyo
 */
public class DocumentoException extends BusinessException {
  public  final static String KEY = "err001";
  public final static String NOTFOUND = "err002";
    
  public DocumentoException()  {
    super(KEY);
  }
  public DocumentoException(String caso){
      super(caso);
  }
}
