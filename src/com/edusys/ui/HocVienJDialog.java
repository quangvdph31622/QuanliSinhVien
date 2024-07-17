/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.edusys.ui;

import com.edusys.DAO.ChuyenDeDAO;
import com.edusys.DAO.HocVienDAO;
import com.edusys.DAO.KhoaHocDAO;
import com.edusys.DAO.NguoiHocDAO;
import com.edusys.entity.ChuyenDe;
import com.edusys.entity.HocVien;
import com.edusys.entity.KhoaHoc;
import com.edusys.entity.NguoiHoc;
import com.edusys.utils.Auth;
import com.edusys.utils.DialogHelper;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class HocVienJDialog extends javax.swing.JDialog {

    ChuyenDeDAO cdDAO = new ChuyenDeDAO();
    KhoaHocDAO khDAO = new KhoaHocDAO();
    NguoiHocDAO nhDAO = new NguoiHocDAO();
    HocVienDAO hvDAO = new HocVienDAO();

    /**
     * Creates new form HocVienJDialog
     */
    public HocVienJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(this);
        fillCboChuyenDe();
    }

    void fillCboChuyenDe() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbochuyende.getModel();
        model.removeAllElements();
        try {
            List<ChuyenDe> list = cdDAO.select();
            for (ChuyenDe chuyenDe : list) {
                model.addElement(chuyenDe);
            }
            this.fillCboKhoaHoc();
        } catch (Exception e) {
            DialogHelper.alert(this, "lỗi truy vấn dữ liệu");
        }

    }

    void fillCboKhoaHoc() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cbokhoahoc.getModel();
        model.removeAllElements();
        try {
            ChuyenDe chuyende = (ChuyenDe) cbochuyende.getSelectedItem();
            if (chuyende != null) {
                List<KhoaHoc> list = khDAO.selectbyChuyenDe(chuyende.getMaCD());
                for (KhoaHoc kh : list) {
                    model.addElement(kh);
                }
            }
            this.fillHocVien();
        } catch (Exception e) {
            DialogHelper.alert(this, "lỗi truy vấn dữ liệu");
        }
    }

    public void fillNguoiHoc() {
        DefaultTableModel model = (DefaultTableModel) tblnguoihoc.getModel();
        model.setRowCount(0);
        try {
            KhoaHoc kh = (KhoaHoc) cbokhoahoc.getSelectedItem();
            if (kh != null) {
                System.out.println("MaKH: " + kh.getMaKH());
                String keyword = txtfind.getText();
                List<NguoiHoc> list = nhDAO.selectNotInCourse(kh.getMaKH(), keyword);
                for (NguoiHoc nh : list) {
                    Object[] row = {
                        nh.getMaNH(),
                        nh.getHoTen(),
                        nh.getNgaySinh(),
                        nh.getGioiTinh() ? "nam" : "nu",
                        nh.getDienThoai(),
                        nh.getEmail()
                    };
                    model.addRow(row);
                }
            }
            fillHocVien();
        } catch (Exception e) {
            DialogHelper.alert(this, "lỗi truy vấn dữ liệu");
        }
    }

    public void fillHocVien() {
        DefaultTableModel model = (DefaultTableModel) tblhocvien.getModel();
        model.setRowCount(0);
        try {
            KhoaHoc kh = (KhoaHoc) cbokhoahoc.getSelectedItem();
            if (kh != null) {
                System.out.println("filltableHocVien MaKH: " + kh.getMaKH());
                List<HocVien> list = hvDAO.selectByKhoaHoc(kh.getMaKH());
                for (int i = 0; i < list.size(); i++) {
                    HocVien hv = list.get(i);
                    String hoTen = nhDAO.findById(hv.getMaNH()).getHoTen();
                    Object[] row = {
                        i + 1,
                        hv.getMaHV(),
                        hv.getMaKH(),
                        hoTen,
                        hv.getDiem()
                    };
                    model.addRow(row);
                }
            }
            fillNguoiHoc();
        } catch (Exception e) {
            DialogHelper.alert(this, "lỗi truy vấn dữ liệu");
        }
    }

    void addHocVien() {
        KhoaHoc khoahoc = (KhoaHoc) cbokhoahoc.getSelectedItem();
        for (int row : tblnguoihoc.getSelectedRows()) {
            HocVien hv = new HocVien();
            hv.setMaKH(khoahoc.getMaKH());
            hv.setDiem(0);
            hv.setMaKH((int) tblnguoihoc.getValueAt(row, 0));
            hvDAO.insert(hv);
        }
        fillHocVien();
    }

    void removeHocVien() {
        if (!Auth.isManager()) {
            DialogHelper.alert(this, "Bạn không đủ quyền để xóa học viên");
        } else {
            if (DialogHelper.confirm(this, "Bạn chắc chắn muốn xóa")) {
                for (int row : tblhocvien.getSelectedRows()) {
                    int maHV = (int) tblhocvien.getValueAt(row, 1);
                    hvDAO.delete(maHV);
                }
                fillHocVien();
            }           
        }
    }

    void updateDiem() {
        for(int i = 0; i< tblhocvien.getRowCount();i++){
            int maHV = (int) tblhocvien.getValueAt(i, 1);
            HocVien hocvien = hvDAO.findById(maHV);
            hocvien.setDiem(Double.parseDouble(tblhocvien.getValueAt(i, 4).toString()));
            hvDAO.update(hocvien);
        }
        DialogHelper.alert(this, "cập nhật thành công");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblnguoihoc = new javax.swing.JTable();
        btnthemkhoahoc = new javax.swing.JButton();
        txtfind = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblhocvien = new javax.swing.JTable();
        btnxoa = new javax.swing.JButton();
        btnsua = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        cbochuyende = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        cbokhoahoc = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Người Học"));

        tblnguoihoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "MA HV", "HO VA TEN", "GIOI TINH", "NGAY SINH", "DIEN THOAI", "EMAIL"
            }
        ));
        tblnguoihoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblnguoihocMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblnguoihoc);

        btnthemkhoahoc.setText("Thêm vào khóa học");
        btnthemkhoahoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnthemkhoahocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(182, 182, 182)
                .addComponent(btnthemkhoahoc, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(txtfind)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(txtfind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(btnthemkhoahoc))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Học Viên"));

        tblhocvien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "MA HV", "MA NH", "HO VA TEN", "DIEM"
            }
        ));
        jScrollPane2.setViewportView(tblhocvien);

        btnxoa.setText("Xóa Học Viên");
        btnxoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnxoaActionPerformed(evt);
            }
        });

        btnsua.setText("Sửa Điểm");
        btnsua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsuaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 606, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(btnxoa)
                .addGap(102, 102, 102)
                .addComponent(btnsua, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnxoa)
                    .addComponent(btnsua))
                .addGap(0, 27, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Chuyên Đề"));

        cbochuyende.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbochuyende.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbochuyendeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbochuyende, 0, 330, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cbochuyende, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Khóa Học"));

        cbokhoahoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbokhoahoc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(cbokhoahoc, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnthemkhoahocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnthemkhoahocActionPerformed
        // TODO add your handling code here:
        addHocVien();
    }//GEN-LAST:event_btnthemkhoahocActionPerformed

    private void cbochuyendeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbochuyendeActionPerformed
        // TODO add your handling code here:
        fillCboKhoaHoc();
    }//GEN-LAST:event_cbochuyendeActionPerformed

    private void btnxoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnxoaActionPerformed
        // TODO add your handling code here:
        removeHocVien();
    }//GEN-LAST:event_btnxoaActionPerformed

    private void btnsuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsuaActionPerformed
        // TODO add your handling code here:
        updateDiem();
    }//GEN-LAST:event_btnsuaActionPerformed

    private void tblnguoihocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblnguoihocMouseClicked
        // TODO add your handling code here:
//        if (evt.getClickCount() == 1) {
//            this.index = tblkhoahoc.rowAtPoint(evt.getPoint());
//            edit();
//        }
    }//GEN-LAST:event_tblnguoihocMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HocVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HocVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HocVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HocVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                HocVienJDialog dialog = new HocVienJDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnsua;
    private javax.swing.JButton btnthemkhoahoc;
    private javax.swing.JButton btnxoa;
    private javax.swing.JComboBox<String> cbochuyende;
    private javax.swing.JComboBox<String> cbokhoahoc;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblhocvien;
    private javax.swing.JTable tblnguoihoc;
    private javax.swing.JTextField txtfind;
    // End of variables declaration//GEN-END:variables
}
