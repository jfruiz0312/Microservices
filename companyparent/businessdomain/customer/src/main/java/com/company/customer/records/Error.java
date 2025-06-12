
package com.company.customer.records;

import java.util.List;

/**
 *
 * @author ruiz_
 */
public record Error(String message, List<ErrorDetail> details) {

}
