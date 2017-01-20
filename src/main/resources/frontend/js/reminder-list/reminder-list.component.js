/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module("reminderList").
        component('reminderList', {
            templateUrl: 'js/reminder-list/reminder-list.template.html',
            controller: function ReminderListController($http) {
                var self = this;
                self.reminders = [];

                $http.get("/reminder").then(function (response) {
                    self.reminders = response.data;
                });

            }
        });
